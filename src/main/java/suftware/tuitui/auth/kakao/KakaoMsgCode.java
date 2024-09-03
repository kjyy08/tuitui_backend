package suftware.tuitui.auth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KakaoMsgCode {
    //  -100
    CODE101(HttpStatus.BAD_REQUEST, "-101", "앱과 카카오계정 연결이 완료되지 않음"),
    CODE102(HttpStatus.BAD_REQUEST, "-102", "이미 앱과 연결된 사용자의 토큰"),
    CODE103(HttpStatus.BAD_REQUEST, "-103", "휴면 상태이거나 없는 카카오 계정"),

    //  -200
    CODE201(HttpStatus.BAD_REQUEST, "-201", "앱에 추가하지 않은 사용자 프로퍼티 키 값"),

    //  -400
    CODE401(HttpStatus.UNAUTHORIZED, "-401", "유효하지 않은 토큰"),
    CODE402(HttpStatus.FORBIDDEN, "-402", "해당 리소스에 접근하려면 사용자의 동의가 필요함"),
    CODE406(HttpStatus.UNAUTHORIZED, "-406", "14세 미만 사용자는 접근이 불가");

    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;

    public static String getMessage(String code){
        for (KakaoMsgCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode.msg;
            }
        }

        return code;
    }
}
