package suftware.tuitui.sns.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class NaverAuthService {
    private static final String AUTH_URI = "https://openapi.naver.com/v1/nid/me";

    public ResponseEntity<NaverResponse> isSignedUp(String accessToken) {
        return RestClient.create().get()
                .uri(AUTH_URI)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((httpRequest, httpResponse) -> {
                    throw new NaverException(httpResponse.getStatusCode(), httpResponse.getBody());
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((httpRequest, httpResponse) -> {
                    throw new NaverException(httpResponse.getStatusCode(), httpResponse.getBody());
                }))
                .toEntity(NaverResponse.class);
    }
}
