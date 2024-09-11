package suftware.tuitui.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.Message;

@Slf4j
@ControllerAdvice
public class TuiTuiExceptionHandler {
    @ExceptionHandler(TuiTuiException.class)
    protected ResponseEntity<Message> handleCustomException(TuiTuiException e){
        log.error("TuiTui Exception Handler -> status: {}, code: {}, message: {}, data: {}",
                e.getMsg().getHttpStatus().toString(), e.getMsg().getCode(), e.getMsg().getMsg(), e.getObj());
        return ResponseEntity.status(e.getMsg().getHttpStatus()).body(Message.builder()
                .status(e.getMsg().getHttpStatus())
                .message(e.getMsg().getMsg())
                .code(e.getMsg().getCode())
                .data(e.getObj())
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Message> handleServerException(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.status(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .code(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getCode())
                .message(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getMsg())
                .build());
    }
}
