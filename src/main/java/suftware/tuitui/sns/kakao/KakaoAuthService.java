package suftware.tuitui.sns.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {
    private static final String AUTH_URI = "https://kapi.kakao.com/v2/user/me";

    public ResponseEntity<KakaoResponse> isSignedUp(String accessToken) {
        return RestClient.create().get()
                .uri(AUTH_URI)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((httpRequest, httpResponse) -> {
                    throw new KakaoException(httpResponse.getStatusCode(), httpResponse.getBody());
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((httpRequest, httpResponse) -> {
                    throw new KakaoException(httpResponse.getStatusCode(), httpResponse.getBody());
                }))
                .toEntity(KakaoResponse.class);
    }
}
