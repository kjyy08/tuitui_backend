package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "capsule_hashtag")
public class TimeCapsuleHashtag {
    @Id
    @Column(name = "capsule_hashtag_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer timeCapsuleHashtagId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id")
    TimeCapsule timeCapsule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "hashtag_id")
    Hashtag hashtag;
}
