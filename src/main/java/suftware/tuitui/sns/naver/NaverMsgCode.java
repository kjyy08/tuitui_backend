package suftware.tuitui.sns.naver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NaverMsgCode {
    //  000
    CODE024(HttpStatus.UNAUTHORIZED, "024", "인증에 실패했습니다"),
    CODE028(HttpStatus.UNAUTHORIZED, "028", "OAuth 인증 헤더가 없음"),

    //  400
    CODE403(HttpStatus.FORBIDDEN, "403", "호출 권한이 없음"),
    CODE404(HttpStatus.NOT_FOUND, "404", "검색 결과가 없음"),

    //  500
    CODE500(HttpStatus.INTERNAL_SERVER_ERROR, "500", "네이버 로그인 데이터베이스 오류");

    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;

    public static String getMessage(String code){
        for (NaverMsgCode errorCode : values()){
            if (errorCode.code.equals(code)){
                return errorCode.msg;
            }
        }

        return code;
    }
}
