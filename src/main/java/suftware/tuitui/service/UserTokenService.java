package suftware.tuitui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.auth.kakao.KakaoAuthService;
import suftware.tuitui.auth.kakao.KakaoResponse;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoAuthService kakaoAuthService;

    //  파라미터로 넘겨받은 account와 소셜 로그인에서 받은 account 비교하여 인증
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String snsType = request.getParameter("sns_type");
        String accessToken = URLDecoder.decode(request.getParameter("access_token"), "UTF-8");
        String account = URLDecoder.decode(request.getParameter("account"), "UTF-8");

        try {
            if (snsType.equals("kakao")) {
                ResponseEntity<KakaoResponse> kakaoResponse = kakaoAuthService.isSignedUp(accessToken);

                //  파라미터로 넘겨받은 account와 카카오 서버에서 받은 account 비교
                if (!account.equals(kakaoResponse.getBody().getKakaoAccount().getEmail())) {
                    return false;
                }
            } else if (snsType.equals("naver")) {

            } else {
                return false;
            }
        } catch (NullPointerException e){
            return false;
        }

        return true;
    }

    //  토큰 발급 요청
    public Message authorization(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String account = URLDecoder.decode(request.getParameter("account"), "UTF-8");

        //  계정 이메일 주소가 없음
        if (account == null || account.isEmpty()){
            throw new JwtException(JwtMsgCode.BAD_REQUEST);
        }

        User user;

        //  계정이 이미 생성됐는지 확인
        if (userRepository.existsByAccount(account)){
            //  이미 생성된 유저는 로그인 처리
            user = userRepository.findByAccount(account)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));
        } else {
            //  유저 정보가 없다면 회원가입 처리
            user = User.builder()
                    .account(account)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            userRepository.save(user);
        }

        UserResponseDto userResponseDto = UserResponseDto.toDTO(user);

        //  DB에 해당 계정이 토큰을 이미 발급받았는지 확인
        if (userTokenRepository.existsByAccount(account)) {
            throw new JwtException(JwtMsgCode.BAD_REQUEST);
        }

        //  토큰 생성
        String access = jwtUtil.createJwt("access", account);   //  1시간의 생명주기를 가짐
        String refresh = jwtUtil.createJwt("refresh", account);  //  30일의 생명주기를 가짐
        UserToken userToken = UserToken.builder()
                .account(account)
                .refresh(refresh)
                .expiresIn(new Timestamp(System.currentTimeMillis() + (jwtUtil.getExpiresIn(refresh) * 1000)))
                .build();

        userTokenRepository.save(userToken);
        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", access, jwtUtil.getExpiresIn(access), refresh, jwtUtil.getExpiresIn(refresh));

        HashMap<String, Object> responseData = new HashMap<>();

        responseData.put("user", userResponseDto);
        responseData.put("token", jwtResponseDto);

        return Message.builder()
                .status(JwtMsgCode.OK.getStatus())
                .code(JwtMsgCode.OK.getCode())
                .message(JwtMsgCode.OK.getMsg())
                .data(responseData)
                .build();
    }

    //  토큰 갱신 요청
    @Transactional
    public Message getRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        try {
            refreshToken = URLDecoder.decode(request.getParameter("refresh_token"), "UTF-8");
        } catch (NullPointerException | UnsupportedEncodingException e) {
            throw new JwtException(JwtMsgCode.EMPTY);
        }

        //  refreshToken 토큰 검증
        JwtMsgCode errorCode = jwtUtil.validateToken(refreshToken);

        if (!errorCode.equals(JwtMsgCode.OK)) {
            throw new JwtException(errorCode);
        }

        //  토큰이 refreshToken 토큰인지 확인
        String tokenType = jwtUtil.getTokenType(refreshToken);

        if (!tokenType.equals("refresh")){
            throw new JwtException(JwtMsgCode.INVALID);
        }

        //  DB에 refreshToken 토큰이 존재하는지 확인
        if (!userTokenRepository.existsByRefresh(refreshToken)){
            throw new JwtException(JwtMsgCode.EXPIRED);
        }

        String account = jwtUtil.getAccount(refreshToken);
        //  엑세스 토큰 발급
        String newAccessToken = jwtUtil.createJwt("access", account);
        //  리프레시 토큰 발급
        String newRefreshToken = jwtUtil.createJwt("refresh", account);

        //  기존에 있던 리프레시 삭제 후 저장
        userTokenRepository.deleteByRefresh(refreshToken);
        userTokenRepository.save(UserToken.builder()
                .account(account)
                .refresh(newRefreshToken)
                .expiresIn(new Timestamp(System.currentTimeMillis() + (jwtUtil.getExpiresIn(newRefreshToken) * 1000)))
                .build());

        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", newAccessToken, jwtUtil.getExpiresIn(newAccessToken),
                newRefreshToken, jwtUtil.getExpiresIn(newRefreshToken));

        return Message.builder()
                .status(JwtMsgCode.OK.getStatus())
                .code(JwtMsgCode.OK.getCode())
                .message(JwtMsgCode.OK.getMsg())
                .data(jwtResponseDto)
                .build();
    }
}
