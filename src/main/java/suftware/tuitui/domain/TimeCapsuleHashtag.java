package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "capsule_hashtag")
public class TimeCapsuleHashtag {
    @Id
    @Column(name = "capsule_hashtag_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer timeCapsuleHashtagId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id", nullable = false)
    TimeCapsule timeCapsule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "hashtag_id", nullable = false)
    Hashtag hashtag;
}
