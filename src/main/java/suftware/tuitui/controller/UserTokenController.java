package suftware.tuitui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.jwt.JwtMsgCode;
import suftware.tuitui.service.UserTokenService;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
@Slf4j
public class UserTokenController {
    private final UserTokenService userTokenService;

    //  jwt 토큰 발급
    @GetMapping(value = "token")
    public ResponseEntity<Message> token(HttpServletRequest request, HttpServletResponse response){
        Message message = null;

        try {
            String grantType = request.getParameter("grant_type");

            //  토큰 발급 요청
            if (grantType.equals("authorization_code")) {
                //  토큰 발급 요청 전 파라미터로 넘겨받은 account 인증
                if (userTokenService.authenticate(request, response)) {
                    message = userTokenService.authorization(request, response);
                } else {
                    message = Message.builder()
                            .status(JwtMsgCode.BAD_REQUEST.getStatus())
                            .message(JwtMsgCode.BAD_REQUEST.getMsg())
                            .code(JwtMsgCode.BAD_REQUEST.getCode())
                            .build();
                }
            }
            //  토큰 갱신 요청
            else if (grantType.equals("refresh")) {
                message = userTokenService.reissue(request, response);
            }
            //  잘못된 접근
            else {
                message = Message.builder()
                        .status(JwtMsgCode.BAD_REQUEST.getStatus())
                        .message(JwtMsgCode.BAD_REQUEST.getMsg())
                        .code(JwtMsgCode.BAD_REQUEST.getCode())
                        .build();
            }
        } catch (NullPointerException e){
            message = Message.builder()
                    .status(JwtMsgCode.BAD_REQUEST.getStatus())
                    .message(JwtMsgCode.BAD_REQUEST.getMsg())
                    .code(JwtMsgCode.BAD_REQUEST.getCode())
                    .build();
        }

        return ResponseEntity.status(message.getStatus()).body(message);
    }

    //  admin, manager 계정 등록
    @GetMapping(value = "token/admin")
    public ResponseEntity<Message> updateRole(@RequestParam(value = "account", required = true) String account,
                                              @RequestParam(value = "role_secret_key", required = true) String roleSecretKey){
        Message message = userTokenService.generateAdminToken(account, roleSecretKey);

        return ResponseEntity.status(message.getStatus()).body(message);
    }
}
