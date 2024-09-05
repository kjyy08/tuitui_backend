package suftware.tuitui.sns.naver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
@Slf4j
public class NaverExceptionHandler {
    @ExceptionHandler(NaverException.class)
    protected ResponseEntity<Message> handleNaverException(NaverException e){
        log.error("TuiTui Exception Handler -> status: {}, code: {}, message: {}",
                e.getHttpStatus().toString(), e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(Message.builder()
                .status(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}
