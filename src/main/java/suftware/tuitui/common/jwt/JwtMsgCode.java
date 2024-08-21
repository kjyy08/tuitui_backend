package suftware.tuitui.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtMsgCode {
    OK(HttpStatus.OK, "OK", "토큰 검증 성공"),
    CREATE_OK(HttpStatus.OK, "OK", "토큰 생성 성공"),
    INVALID(HttpStatus.UNAUTHORIZED, "INVALID", "토큰의 구성이 잘못됨"),
    EXPIRED(HttpStatus.UNAUTHORIZED, "EXPIRED", "토큰이 만료됨"),
    UNSUPPORTED(HttpStatus.UNAUTHORIZED, "UNSUPPORTED", "지원하지 않는 토큰"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "인가받지 못한 토큰"),
    EMPTY(HttpStatus.UNAUTHORIZED, "EMPTY", "토큰이 존재하지 않음");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
