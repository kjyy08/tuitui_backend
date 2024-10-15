package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "capsule_like")
public class TimeCapsuleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_like_id", nullable = false, unique = true)
    Integer capsuleLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "profile_id", unique = true, nullable = false)
    Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id", unique = true, nullable = false)
    TimeCapsule timeCapsule;

    public static TimeCapsuleLike of(TimeCapsule timeCapsule, Profile profile){
        return TimeCapsuleLike.builder()
                .timeCapsule(timeCapsule)
                .profile(profile)
                .build();
    }
}
