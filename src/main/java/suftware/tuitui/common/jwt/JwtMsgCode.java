package suftware.tuitui.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtMsgCode {
    // 200 OK responses
    OK(HttpStatus.OK, "JWT-001", "토큰 발급 성공"),

    //  400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "JWT-002", "잘못된 토큰 발급 요청"),
    UNSUPPORTED(HttpStatus.BAD_REQUEST, "JWT-003", "지원하지 않는 토큰"),
    INVALID(HttpStatus.BAD_REQUEST, "JWT-004", "토큰의 구성이 잘못됨"),

    // 401 Unauthorized responses
    EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-005", "토큰이 만료됨"),

    // 403 Forbidden responses
    FORBIDDEN(HttpStatus.FORBIDDEN, "JWT-006", "인가받지 못한 토큰"),

    // 404 Not Found responses
    EMPTY(HttpStatus.BAD_REQUEST, "JWT-007", "토큰이 존재하지 않음");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
