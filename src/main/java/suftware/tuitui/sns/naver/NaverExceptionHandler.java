package suftware.tuitui.sns.naver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
public class NaverExceptionHandler {
    @ExceptionHandler(NaverException.class)
    protected ResponseEntity<Message> handleKakaoException(NaverException e){
        return ResponseEntity.status(e.getHttpStatus()).body(Message.builder()
                .status(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}
