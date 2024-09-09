package suftware.tuitui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.sns.kakao.KakaoAuthService;
import suftware.tuitui.sns.kakao.KakaoResponse;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.jwt.JwtException;
import suftware.tuitui.common.jwt.JwtMsgCode;
import suftware.tuitui.common.jwt.JwtResponseDto;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.domain.User;
import suftware.tuitui.domain.UserToken;
import suftware.tuitui.dto.response.UserResponseDto;
import suftware.tuitui.repository.UserRepository;
import suftware.tuitui.repository.UserTokenRepository;
import suftware.tuitui.sns.naver.NaverAuthService;
import suftware.tuitui.sns.naver.NaverResponse;


import java.sql.Timestamp;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTokenService {
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoAuthService kakaoAuthService;
    private final NaverAuthService naverAuthService;

    //  파라미터로 넘겨받은 account와 소셜 로그인에서 받은 account 비교하여 인증
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String snsType = request.getParameter("sns_type");
            String accessToken = request.getParameter("access_token");
            String account = request.getParameter("account");
            log.info("UserTokenService.authenticate() -> request.getParameter(), sns_type: {}, access_token: {}, account: {}",
                    snsType, accessToken, account);

            if (snsType.equals("kakao")) {
                ResponseEntity<KakaoResponse> kakaoResponse = kakaoAuthService.isSignedUp(accessToken);

                //  파라미터로 넘겨받은 account와 카카오 서버에서 받은 account 비교
                if (!account.equals(kakaoResponse.getBody().getKakaoAccount().getEmail())) {
                    log.error("UserTokenService.authenticate() -> kakao authenticate fail, account: {}", account);
                    return false;
                }
            } else if (snsType.equals("naver")) {
                ResponseEntity<NaverResponse> naverResponse = naverAuthService.isSignedUp(accessToken);

                //  파라미터로 넘겨받은 account와 카카오 서버에서 받은 account 비교
                if (!account.equals(naverResponse.getBody().getNaverAccount().getEmail())) {
                    log.error("UserTokenService.authenticate() -> naver authenticate fail, account: {}", account);
                    return false;
                }
            } else {
                log.error("UserTokenService.authenticate() -> snsType: {} not found", snsType);
                return false;
            }
        } catch (NullPointerException e){
            log.error("UserTokenService.authenticate() -> request.getParameter() is null");
            return false;
        }

        log.info("UserTokenService.authenticate() -> success");
        return true;
    }

    //  토큰 발급 요청
    public Message authorization(HttpServletRequest request, HttpServletResponse response) {
        String account;
        String snsType;

        try {
            account = request.getParameter("account");
            snsType = request.getParameter("sns_type");
            log.info("UserTokenService.authorization() -> request.getParameter(), account: {}, snsType: {}", account, snsType);
        } catch (NullPointerException e){
            log.error("UserTokenService.authorization() -> request.getParameter() is null");
            throw new JwtException(JwtMsgCode.BAD_REQUEST);
        }

        User user;
        boolean isSigned;

        //  계정이 이미 생성됐는지 확인
        if (userRepository.existsByAccount(account)){
            //  이미 생성된 유저는 기존 유저 정보 반환
            //  account, sns 일치 여부 확인 후 불일치하면 예외 발생
            user = userRepository.findByAccountAndSnsType(account, snsType)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_EXIST));
            isSigned = true;
            log.info("UserTokenService.authorization() -> account: {} is exists", account);
        } else {
            //  유저 정보가 없다면 회원가입 처리
            user = User.builder()
                    .account(account)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .role(Role.USER)
                    .snsType(snsType)
                    .build();

            userRepository.save(user);
            isSigned = false;
            log.info("UserTokenService.authorization() -> create account: {}", account);
        }

        //  DB에 해당 계정이 토큰을 이미 발급받았는지 확인
        if (userTokenRepository.existsByAccount(account)) {
            log.error("UserTokenService.authorization() -> account: {} already logged in", account);
            throw new JwtException(JwtMsgCode.BAD_REQUEST);
        }

        //  토큰 생성
        String access = jwtUtil.createJwt("access", account, Role.USER.getValue());   //  1시간의 생명주기를 가짐
        String refresh = jwtUtil.createJwt("refresh", account, Role.USER.getValue());  //  30일의 생명주기를 가짐
        UserToken userToken = UserToken.builder()
                .account(account)
                .refresh(refresh)
                .expiresIn(new Timestamp(System.currentTimeMillis() + (jwtUtil.getExpiresIn(refresh) * 1000)))
                .build();

        userTokenRepository.save(userToken);
        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", access, jwtUtil.getExpiresIn(access), refresh, jwtUtil.getExpiresIn(refresh));

        //  response body 응답 생성
        Message message = new Message();
        UserResponseDto userResponseDto = UserResponseDto.toDTO(user);
        HashMap<String, Object> responseData = new HashMap<>();

        responseData.put("user", userResponseDto);
        responseData.put("token", jwtResponseDto);

        //  신규 유저면 http status 201, 기존 유저면 200 응답
        if (!isSigned){
            message.setStatus(TuiTuiMsgCode.USER_SIGNUP_SUCCESS.getHttpStatus());
            message.setCode(JwtMsgCode.OK.getCode());
            message.setMessage(JwtMsgCode.OK.getMsg());
            message.setData(responseData);
        } else {
            message.setStatus(JwtMsgCode.OK.getStatus());
            message.setCode(JwtMsgCode.OK.getCode());
            message.setMessage(JwtMsgCode.OK.getMsg());
            message.setData(responseData);
        }

        log.info("UserTokenService.authorization() -> success");
        return message;
    }

    //  토큰 갱신 요청
    @Transactional
    public Message getRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken;

        try {
            refreshToken = request.getParameter("refresh_token");
        } catch (NullPointerException e) {
            throw new JwtException(JwtMsgCode.EMPTY);
        }

        log.info("UserTokenService.getRefreshToken() -> request.getParameter(), refreshToken: {}", refreshToken);

        //  refreshToken 토큰 검증
        JwtMsgCode errorCode = jwtUtil.validateToken(refreshToken);

        if (!errorCode.equals(JwtMsgCode.OK)) {
            throw new JwtException(errorCode);
        }

        //  토큰이 refreshToken 토큰인지 확인
        String tokenType = jwtUtil.getTokenType(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        if (!tokenType.equals("refresh")){
            throw new JwtException(JwtMsgCode.INVALID);
        }

        //  DB에 refreshToken 토큰이 존재하는지 확인
        if (!userTokenRepository.existsByRefresh(refreshToken)){
            throw new JwtException(JwtMsgCode.EXPIRED);
        }

        String account = jwtUtil.getAccount(refreshToken);
        //  엑세스 토큰 발급
        String newAccessToken = jwtUtil.createJwt("access", account, role);
        //  리프레시 토큰 발급
        String newRefreshToken = jwtUtil.createJwt("refresh", account, role);

        //  기존에 있던 리프레시 삭제 후 저장
        userTokenRepository.deleteByRefresh(refreshToken);
        userTokenRepository.save(UserToken.builder()
                .account(account)
                .refresh(newRefreshToken)
                .expiresIn(new Timestamp(System.currentTimeMillis() + (jwtUtil.getExpiresIn(newRefreshToken) * 1000)))
                .build());

        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", newAccessToken, jwtUtil.getExpiresIn(newAccessToken),
                newRefreshToken, jwtUtil.getExpiresIn(newRefreshToken));

        log.info("UserTokenService.getRefreshToken() -> success");
        return Message.builder()
                .status(JwtMsgCode.OK.getStatus())
                .code(JwtMsgCode.OK.getCode())
                .message(JwtMsgCode.OK.getMsg())
                .data(jwtResponseDto)
                .build();
    }
}
