package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
    Integer userId;

    @Column(name = "account", length = 60, nullable = false, unique = true)
    String account;

    @Column(name = "password", nullable = false, length = 60)
    String password;

    @Column(name = "phone", nullable = false, length = 13)
    String phone;

    @Column(name = "name", nullable = false, length = 16)
    String name;

    @Column(name = "account_created_date")
    Timestamp accountCreatedDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;
}
