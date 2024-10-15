package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.enumType.AccountState;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.common.time.DateTimeUtil;

import java.sql.Timestamp;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "account", length = 60, nullable = false, unique = true)
    private String account;

    @Column(name = "password", length = 300)
    private String password;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_state")
    private AccountState accountState;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "sns_type")
    private String snsType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    public static User of(String account, String snsType, AccountState state, Role role){
        return User.builder()
                .account(account)
                .createdAt(DateTimeUtil.getSeoulTimestamp())
                .accountState(state)
                .role(role)
                .snsType(snsType.toLowerCase())
                .build();
    }

}
