package suftware.tuitui.auth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
public class KakaoException extends RuntimeException  {
    private final HttpStatus httpStatus;
    private final Map<String, Object> body;

    public KakaoException(HttpStatusCode httpStatus, InputStream body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.httpStatus = HttpStatus.valueOf(httpStatus.value());
        this.body = objectMapper.readValue(body, Map.class);
    }
}
