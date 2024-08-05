package suftware.tuitui.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Message> handleCustomException(CustomException e){
        return ResponseEntity.status(e.getMsgCode().getHttpStatus()).body(Message.builder()
                .status(e.getMsgCode().getHttpStatus())
                .message(e.getMsgCode().getMsg())
                .build());
    }
}
