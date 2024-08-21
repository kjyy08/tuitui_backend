package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "user_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false, unique = true)
    private Integer tokenId;

    @Column(name = "account", length = 60, nullable = false, unique = true)
    private String account;

    @Column(name = "refresh_token", length = 200, nullable = false)
    private String refresh;

    @Column(name = "expiration", nullable = false)
    Date expiration;

}
