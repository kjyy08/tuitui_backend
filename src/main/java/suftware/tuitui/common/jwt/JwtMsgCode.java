package suftware.tuitui.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtMsgCode {
    // 200 OK responses
    OK(HttpStatus.OK, "JWT-001", "토큰 검증 성공"),
    CREATE_OK(HttpStatus.OK, "JWT-002", "토큰 생성 성공"),

    // 401 Unauthorized responses
    INVALID(HttpStatus.UNAUTHORIZED, "JWT-003", "토큰의 구성이 잘못됨"),
    EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-004", "토큰이 만료됨"),
    UNSUPPORTED(HttpStatus.UNAUTHORIZED, "JWT-005", "지원하지 않는 토큰"),

    // 403 Forbidden responses
    FORBIDDEN(HttpStatus.FORBIDDEN, "JWT-006", "인가받지 못한 토큰"),

    // 404 Not Found responses
    EMPTY(HttpStatus.NOT_FOUND, "JWT-007", "토큰이 존재하지 않음");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
