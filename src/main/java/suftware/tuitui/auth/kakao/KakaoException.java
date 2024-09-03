package suftware.tuitui.auth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import suftware.tuitui.common.http.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
public class KakaoException extends RuntimeException  {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public KakaoException(HttpStatusCode httpStatus, InputStream body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.httpStatus = HttpStatus.valueOf(httpStatus.value());
        Map<String, Object> jsonMap = objectMapper.readValue(body, Map.class);

        try {
            this.code = jsonMap.get("code").toString();
            this.message = KakaoMsgCode.getMessage(jsonMap.get("code").toString());
        } catch (Exception e){
            throw new RuntimeException(e);
        }


    }
}
