package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.time.DateTimeUtil;

import java.sql.Timestamp;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "refresh_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", nullable = false, unique = true)
    Integer tokenId;

    @Column(name = "account", length = 60, nullable = false)
    String account;

    @Column(name = "refresh_token", length = 512, nullable = false, unique = true)
    String refresh;

    @Column(name = "expires_at", nullable = false)
    Timestamp expiresIn;

    public static UserToken of(String account, String refresh, Long expiresIn){
        return UserToken.builder()
                .account(account)
                .refresh(refresh)
                .expiresIn(new Timestamp(DateTimeUtil.getSeoulTimestamp().getTime() + expiresIn))
                .build();
    }
}
