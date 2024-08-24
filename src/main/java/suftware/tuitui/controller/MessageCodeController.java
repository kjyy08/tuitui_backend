package suftware.tuitui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.common.jwt.JwtMsgCode;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messageCodes")
public class MessageCodeController {

    @GetMapping(value = "/MsgCode")
    public ResponseEntity<Map<String, String>> getAllMessages(){
        return ResponseEntity.ok(
                Arrays.stream(MsgCode.values())
                        .collect(Collectors.toMap(MsgCode::getCode, MsgCode::getMsg))
        );
    }

    @GetMapping(value = "/JwtCode")
    public ResponseEntity<Map<String, String>> getJWTMessages() {
        return ResponseEntity.ok(
                Arrays.stream(JwtMsgCode.values())
                        .collect(Collectors.toMap(JwtMsgCode::getCode, JwtMsgCode::getMsg))
        );
    }
}