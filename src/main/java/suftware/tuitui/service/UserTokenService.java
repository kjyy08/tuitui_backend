package suftware.tuitui.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.jwt.JwtMsgCode;
import suftware.tuitui.common.jwt.JwtResponseDto;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.domain.UserToken;
import suftware.tuitui.repository.UserTokenRepository;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Message getToken(String refreshToken) {
        //  refreshToken 토큰이 존재하지 않는 경우
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.EMPTY.getCode())
                    .message(JwtMsgCode.EMPTY.getMsg())
                    .build();
        }

        //  토큰이 refreshToken 토큰인지 확인
        String tokenType = jwtUtil.getTokenType(refreshToken);

        if (!tokenType.equals("refresh")){
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.INVALID.getCode())
                    .message(JwtMsgCode.INVALID.getMsg())
                    .build();
        }

        //  refreshToken 토큰 검증
        JwtMsgCode errorCode = jwtUtil.validateToken(refreshToken);

        if (!errorCode.equals(JwtMsgCode.OK)) {
            return Message.builder()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(errorCode.getMsg())
                    .build();
        }

        //  DB에 refreshToken 토큰이 존재하는지 확인
        if (!userTokenRepository.existsByRefresh(refreshToken)){
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.EXPIRED.getCode())
                    .message(JwtMsgCode.EXPIRED.getMsg())
                    .build();
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
                .expiresIn(new Timestamp(jwtUtil.getExpiresIn(newRefreshToken) * 1000))
                .build());

        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", newAccessToken, jwtUtil.getExpiresIn(newAccessToken),
                newRefreshToken, jwtUtil.getExpiresIn(newRefreshToken));

        //  24.08.29 토큰 응답 body로 옮기면서 쿠키 사용 안함.
        //  쿠키에 refreshToken 토큰 저장
        //  Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        //  cookie.setMaxAge(2592000);  //  30일까지 유효
        //  cookie.setHttpOnly(true);
        //  cookie.setSecure(true);

        return Message.builder()
                .status(HttpStatus.OK)
                .code(JwtMsgCode.CREATE_OK.getCode())
                .message(JwtMsgCode.CREATE_OK.getMsg())
                .data(jwtResponseDto)
                .build();
    }

}
