package suftware.tuitui.sns.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import suftware.tuitui.sns.kakao.KakaoMsgCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
public class NaverException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public NaverException(HttpStatusCode httpStatus, InputStream body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.httpStatus = HttpStatus.valueOf(httpStatus.value());
        Map<String, Object> jsonMap = objectMapper.readValue(body, Map.class);

        try {
            this.code = jsonMap.get("resultcode").toString();
            this.message = NaverMsgCode.getMessage(jsonMap.get("resultcode").toString());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
