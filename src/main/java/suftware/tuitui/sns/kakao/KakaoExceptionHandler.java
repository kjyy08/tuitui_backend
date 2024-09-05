package suftware.tuitui.sns.kakao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
public class KakaoExceptionHandler{
    @ExceptionHandler(KakaoException.class)
    protected ResponseEntity<Message> handleKakaoException(KakaoException e){
        return ResponseEntity.status(e.getHttpStatus()).body(Message.builder()
                .status(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}
