package suftware.tuitui.sns.naver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import suftware.tuitui.common.http.HttpResponseDto;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NaverExceptionHandler {
    @ExceptionHandler(NaverException.class)
    protected ResponseEntity<HttpResponseDto> handleNaverException(NaverException e){
        log.error("NaverExceptionHandler.handleNaverException() -> status: {}, code: {}, message: {}",
                e.getHttpStatus().toString(), e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(HttpResponseDto.builder()
                .status(e.getHttpStatus())
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}
