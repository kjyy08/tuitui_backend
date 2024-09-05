package suftware.tuitui.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
@Slf4j
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Message> handleJwtException(JwtException e){
        log.error("Jwt Exception Handler -> status: {}, code: {}, message: {}",
                e.getMsg().getStatus().toString(), e.getMsg().getCode(), e.getMsg().getMsg());
        return ResponseEntity.status(e.getMsg().getStatus()).body(Message.builder()
                .status(e.getMsg().getStatus())
                .code(e.getMsg().getCode())
                .message(e.getMsg().getMsg())
                .build());
    }
}
