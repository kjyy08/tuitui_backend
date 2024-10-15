package suftware.tuitui.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.jwt.JwtMsgCode;

@Setter
@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {
    private HttpStatus status;
    private String message;
    private String code;
    private Object data;

    public HttpResponse(){
        this.status = null;
        this.message = null;
        this.code = null;
        this.data = null;
    }

    public HttpResponse(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public static HttpResponse toBody(TuiTuiMsgCode tuiTuiMsgCode){
        return HttpResponse.builder()
                .status(tuiTuiMsgCode.getHttpStatus())
                .code(tuiTuiMsgCode.getCode())
                .message(tuiTuiMsgCode.getMsg())
                .build();
    }

    public static HttpResponse toBody(TuiTuiMsgCode tuiTuiMsgCode, Object obj){
        return HttpResponse.builder()
                .status(tuiTuiMsgCode.getHttpStatus())
                .code(tuiTuiMsgCode.getCode())
                .message(tuiTuiMsgCode.getMsg())
                .data(obj)
                .build();
    }

    public static HttpResponse toBody(JwtMsgCode jwtMsgCode){
        return HttpResponse.builder()
                .status(jwtMsgCode.getStatus())
                .code(jwtMsgCode.getCode())
                .message(jwtMsgCode.getMsg())
                .build();
    }

    public static HttpResponse toBody(JwtMsgCode jwtMsgCode, Object obj){
        return HttpResponse.builder()
                .status(jwtMsgCode.getStatus())
                .code(jwtMsgCode.getCode())
                .message(jwtMsgCode.getMsg())
                .data(obj)
                .build();
    }

    public static ResponseEntity<HttpResponse> toResponseEntity(TuiTuiMsgCode tuiTuiMsgCode){
        return ResponseEntity.status(tuiTuiMsgCode.getHttpStatus()).body(toBody(tuiTuiMsgCode));
    }

    public static ResponseEntity<HttpResponse> toResponseEntity(TuiTuiMsgCode tuiTuiMsgCode, Object obj){
        return ResponseEntity.status(tuiTuiMsgCode.getHttpStatus()).body(toBody(tuiTuiMsgCode, obj));
    }
}
