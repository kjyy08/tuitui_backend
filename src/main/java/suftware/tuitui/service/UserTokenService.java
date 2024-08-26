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
    public Message reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        //  쿠키에 담긴 refresh 토큰을 가져옴
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //  refresh 토큰이 존재하지 않는 경우
        if (refresh == null) {
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.EMPTY.getCode())
                    .message(JwtMsgCode.EMPTY.getMsg())
                    .build();
        }

        //  refresh 토큰 검증
        JwtMsgCode errorCode = jwtUtil.validateToken(refresh);

        if (!errorCode.equals(JwtMsgCode.OK)) {
            return Message.builder()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(errorCode.getMsg())
                    .build();
        }

        //  토큰이 refresh 토큰인지 확인
        String tokenType = jwtUtil.getTokenType(refresh);

        if (!tokenType.equals("refresh")){
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.INVALID.getCode())
                    .message(JwtMsgCode.INVALID.getMsg())
                    .build();
        }

        //  DB에 refresh 토큰이 존재하는지 확인
        if (!userTokenRepository.existsByRefresh(refresh)){
            return Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(JwtMsgCode.INVALID.getCode())
                    .message(JwtMsgCode.INVALID.getMsg())
                    .build();
        }

        String account = jwtUtil.getAccount(refresh);
        //  엑세스 토큰 발급
        String newAccessToken = jwtUtil.createJwt("access", account);
        //  리프레시 토큰 발급
        String newRefreshToken = jwtUtil.createJwt("refresh", account);

        //  기존에 있던 리프레시 삭제 후 저장
        userTokenRepository.deleteByRefresh(refresh);
        userTokenRepository.save(UserToken.builder()
                .account(account)
                .refresh(newRefreshToken)
                .expiresIn(new Timestamp(System.currentTimeMillis() + jwtUtil.getRefreshTokenExpiresIn()))
                .build());

        //  쿠키에 refresh 토큰 저장
        Cookie cookie = new Cookie("refresh", newRefreshToken);
        cookie.setMaxAge(2592000);  //  30일까지 유효
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(cookie);

        return Message.builder()
                .status(HttpStatus.OK)
                .code(JwtMsgCode.CREATE_OK.getCode())
                .message(JwtMsgCode.CREATE_OK.getMsg())
                .build();
    }

}
