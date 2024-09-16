package suftware.tuitui.sns.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import suftware.tuitui.common.http.Message;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KakaoExceptionHandler{
    @ExceptionHandler(KakaoException.class)
    protected ResponseEntity<Message> handleKakaoException(KakaoException e){
        log.error("KakaoExceptionHandler.handleKakaoException() -> status: {}, code: {}, message: {}",
                e.getHttpStatus().toString(), e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(Message.builder()
                .status(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}
