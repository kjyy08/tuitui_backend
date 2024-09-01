package suftware.tuitui.common.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Message> handleJwtException(JwtException e){
        return ResponseEntity.status(e.getMsg().getStatus()).body(Message.builder()
                .status(e.getMsg().getStatus())
                .code(e.getMsg().getCode())
                .message(e.getMsg().getMsg())
                .build());
    }
}
