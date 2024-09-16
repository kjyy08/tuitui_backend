package suftware.tuitui.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.Message;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //  앱 관련 오류 예외처리 핸들러
    @ExceptionHandler(TuiTuiException.class)
    protected ResponseEntity<Message> handleTuiTuiException(TuiTuiException e){
        log.error("TuiTuiExceptionHandler.handleTuiTuiException() -> status: {}, code: {}, message: {}, data: {}",
                e.getMsg().getHttpStatus().toString(), e.getMsg().getCode(), e.getMsg().getMsg(), e.getObj());
        return ResponseEntity.status(e.getMsg().getHttpStatus()).body(Message.builder()
                .status(e.getMsg().getHttpStatus())
                .message(e.getMsg().getMsg())
                .code(e.getMsg().getCode())
                .data(e.getObj())
                .build());
    }

    //  서버 스크립트 오류 예외처리 핸들러
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Message> handleServerException(RuntimeException e){
        log.error("TuiTuiExceptionHandler.handleServerException() -> error Message: {}", e.getMessage());
        return ResponseEntity.status(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .code(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getCode())
                .message(TuiTuiMsgCode.INTERNAL_SERVER_ERROR.getMsg())
                .build());
    }

    //  유효성 검사 예외처리 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Message> handleValidException(MethodArgumentNotValidException e){
        TuiTuiMsgCode tuiTuiMsgCode = getTuiTuiErrorCode(e.getBindingResult());
        HashMap<String, String> errors = getErrors(e.getBindingResult());

        log.error("TuiTuiExceptionHandler.handleValidException() -> error Message: {}", errors);
        log.error("TuiTuiExceptionHandler.handleValidException() -> rejected Values: {}",
                e.getFieldErrors().stream()
                        .map(fieldError -> String.format("\"%s\": \"%s\"", fieldError.getField(), fieldError.getRejectedValue()))
                        .collect(Collectors.toList()));
        return ResponseEntity.status(tuiTuiMsgCode.getHttpStatus()).body(Message.builder()
                .status(tuiTuiMsgCode.getHttpStatus())
                .code(tuiTuiMsgCode.getCode())
                .message(tuiTuiMsgCode.getMsg())
                .data(errors)
                .build());
    }

    //  유효성 검사 실패시 타겟의 코드 반환
    private TuiTuiMsgCode getTuiTuiErrorCode(BindingResult bindingResult){
        String target = Objects.requireNonNull(bindingResult.getTarget()).toString().toLowerCase();

        if (target.contains("user")){
            return TuiTuiMsgCode.USER_NOT_VALID;
        } else if(target.contains("profile")){
            return TuiTuiMsgCode.PROFILE_NOT_VALID;
        } else if(target.contains("comment")){
            return TuiTuiMsgCode.COMMENT_NOT_VALID;
        } else {
            log.error("TuiTuiExceptionHandler.getTuiTuiErrorCode() -> unexpected Validation Target: {}", target);
            return TuiTuiMsgCode.INTERNAL_SERVER_ERROR;
        }
    }

    //  유효성 검사 실패 목록 반환
    private HashMap<String, String> getErrors(BindingResult bindingResult) {
        HashMap<String, String> validatorResult = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validatorResult.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return validatorResult;
    }
}
