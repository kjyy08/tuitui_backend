package suftware.tuitui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.jwt.JwtException;
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
    public ResponseEntity<HttpResponse> token(HttpServletRequest request, HttpServletResponse response){
        HttpResponse httpResponse;

        try {
            String grantType = request.getParameter("grant_type");

            //  토큰 발급 요청
            if (grantType.equals("authorization_code")) {
                //  토큰 발급 요청 전 파라미터로 넘겨받은 account 인증
                if (userTokenService.authenticate(request, response)) {
                    httpResponse = userTokenService.authorization(request, response);
                } else {
                    throw new JwtException(JwtMsgCode.BAD_REQUEST);
                }
            }
            //  토큰 갱신 요청
            else if (grantType.equals("refresh")) {
                httpResponse = userTokenService.reissue(request, response);
            }
            //  잘못된 접근
            else {
                throw new JwtException(JwtMsgCode.BAD_REQUEST);
            }
        } catch (NullPointerException e){
            throw new JwtException(JwtMsgCode.BAD_REQUEST);
        }

        return ResponseEntity.status(httpResponse.getStatus()).body(httpResponse);
    }

    //  admin, manager 계정 등록
    @GetMapping(value = "token/admin")
    public ResponseEntity<HttpResponse> updateRole(@RequestParam(value = "account", required = true) String account,
                                                   @RequestParam(value = "role_secret_key", required = true) String roleSecretKey){
        HttpResponse httpResponse = userTokenService.generateAdminToken(account, roleSecretKey);

        return ResponseEntity.status(httpResponse.getStatus()).body(httpResponse);
    }
}
