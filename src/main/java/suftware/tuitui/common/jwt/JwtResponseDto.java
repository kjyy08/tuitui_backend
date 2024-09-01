package suftware.tuitui.common.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseDto {
    String tokenType;
    String accessToken;
    Long accessTokenExpiresIn;
    String refreshToken;
    Long refreshTokenExpiresIn;

    public static JwtResponseDto toDto(String tokenType, String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn){
        return JwtResponseDto.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }
}
