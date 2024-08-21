package suftware.tuitui.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret-key}") String secretKey){
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //  token, secretKey를 이용하여 검증 후 payload에 저장된 "account" 반환
    public String getAccount(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("account", String.class);
    }

    //  token, secretKey를 이용하여 검증 후 현재 시간과 비교하여 token 소멸 여부 확인
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //  토큰이 access인지 refresh인지 확인
    public String getTokenType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("type", String.class);
    }

    // 토큰 정보를 검증하는 메서드
    public JwtMsgCode validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return JwtMsgCode.OK;
        } catch (SignatureException | SecurityException | MalformedJwtException e) {
            return JwtMsgCode.INVALID;
        } catch (ExpiredJwtException e) {
            return JwtMsgCode.EXPIRED;
        } catch (UnsupportedJwtException e) {
            return JwtMsgCode.UNSUPPORTED;
        } catch (IllegalArgumentException e) {
            return JwtMsgCode.EMPTY;
        }
    }

    //  토큰 생성
    public String createJwt(String type, String account, Long expiredTime){
        return Jwts.builder()
                //  클레임에 토큰 타입 저장
                .claim("type", type)
                //  클레임에 account 저장
                .claim("account", account)
                //  토큰의 발행 시간
                .issuedAt(new Date(System.currentTimeMillis()))
                //  토큰의 소멸 시간
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                //  jwt 시그니처에 secretKey 설정
                .signWith(secretKey)
                .compact();
    }
}
