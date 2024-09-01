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
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "JWT-003", "잘못된 토큰 발급 요청"),

    // 401 Unauthorized responses
    INVALID(HttpStatus.UNAUTHORIZED, "JWT-004", "토큰의 구성이 잘못됨"),
    EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-005", "토큰이 만료됨"),
    UNSUPPORTED(HttpStatus.UNAUTHORIZED, "JWT-006", "지원하지 않는 토큰"),

    // 403 Forbidden responses
    FORBIDDEN(HttpStatus.FORBIDDEN, "JWT-007", "인가받지 못한 토큰"),

    // 404 Not Found responses
    EMPTY(HttpStatus.NOT_FOUND, "JWT-008", "토큰이 존재하지 않음");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
