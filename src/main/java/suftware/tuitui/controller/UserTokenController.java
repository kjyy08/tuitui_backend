package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.service.UserTokenService;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
@Slf4j
public class UserTokenController {
    private final UserTokenService userTokenService;

    //  jwt 토큰 재발급
    @PostMapping(value = "token")
    public ResponseEntity<Message> token(@RequestParam(name = "refreshToken") String refreshToken){
        Message message = userTokenService.getToken(refreshToken);

        return ResponseEntity.status(message.getStatus()).body(message);

        //  24.08.21
        //  아래에 주석 내용들 service로 옮김
//        String refresh = null;
//
//        //  쿠키에 담긴 refresh 토큰을 가져옴
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("refresh")) {
//                refresh = cookie.getValue();
//            }
//        }
//
//        //  refresh 토큰이 존재하지 않는 경우
//        if (refresh == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
//                    .status(HttpStatus.BAD_REQUEST)
//                    .message(JwtMsgCode.EMPTY.getMsg())
//                    .build());
//        }
//
//        //  refresh 토큰 검증
//        JwtMsgCode errorCode = jwtUtil.validateToken(refresh);
//
//        if (!errorCode.equals(JwtMsgCode.OK)) {
//            return ResponseEntity.status(errorCode.getStatus()).body(Message.builder()
//                    .status(errorCode.getStatus())
//                    .message(errorCode.getMsg())
//                    .build());
//        }
//
//        //  토큰이 refresh 토큰인지 확인
//        String tokenType = jwtUtil.getTokenType(refresh);
//
//        if (!tokenType.equals("refresh")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
//                    .status(HttpStatus.BAD_REQUEST)
//                    .message(JwtMsgCode.INVALID.getMsg())
//                    .build());
//        }
//
//        String account = jwtUtil.getAccount(refresh);
//        //  엑세스 토큰 발급
//        String newAccessToken = jwtUtil.createJwt("access", account, 600000L);
//        //  리프레시 토큰 발급
//        String newRefreshToken = jwtUtil.createJwt("refresh", account, 86400000L);
//
//        //  쿠키에 refresh 토큰 저장
//        Cookie cookie = new Cookie("refresh", newRefreshToken);
//        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setHttpOnly(true);
//
//        response.setHeader("access", "Bearer " + newAccessToken);
//        response.addCookie(cookie);
//
//        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
//                .status(HttpStatus.OK)
//                .message(JwtMsgCode.CREATE_OK.getMsg())
//                .build());
    }
}
