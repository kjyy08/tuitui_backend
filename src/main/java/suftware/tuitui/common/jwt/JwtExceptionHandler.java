package suftware.tuitui.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import suftware.tuitui.common.http.HttpResponse;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)  //  RunTimeException 핸들러보다 Bean 우선순위를 높이 둠
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<HttpResponse> handleJwtException(JwtException e){
        log.error("JwtExceptionHandler.handleJwtException() -> status: {}, code: {}, message: {}",
                e.getMsg().getStatus().toString(), e.getMsg().getCode(), e.getMsg().getMsg());
        return ResponseEntity.status(e.getMsg().getStatus()).body(HttpResponse.builder()
                .status(e.getMsg().getStatus())
                .code(e.getMsg().getCode())
                .message(e.getMsg().getMsg())
                .build());
    }
}
