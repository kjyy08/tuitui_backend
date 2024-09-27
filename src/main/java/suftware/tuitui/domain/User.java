package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.enumType.AccountState;
import suftware.tuitui.common.enumType.Role;

import java.sql.Timestamp;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
