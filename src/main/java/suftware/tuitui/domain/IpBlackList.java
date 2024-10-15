package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import suftware.tuitui.common.time.DateTimeUtil;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ip_blacklist")
public class IpBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_id", nullable = false, unique = true)
    private Integer ipId;

    @Column(name = "ip_address", nullable = false, unique = true, length = 45)
    private String ipAddress;

    @Column(name = "banned", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean isBanned;

    @Column(name = "banned_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp bannedAt;

    public static IpBlackList of(String ip) {
        return IpBlackList.builder()
                .ipAddress(ip)
                .bannedAt(DateTimeUtil.getSeoulTimestamp())
                .isBanned(true)
                .build();
    }
}
