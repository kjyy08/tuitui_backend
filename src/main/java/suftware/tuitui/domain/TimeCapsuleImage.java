package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "capsule_image")
public class TimeCapsuleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false, unique = true)
    Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id", nullable = false, unique = true)
    TimeCapsule timeCapsule;

    @Column(name = "image_path", nullable = false)
    String imgUrl;

    public static TimeCapsuleImage of(TimeCapsule timeCapsule, String url){
        return TimeCapsuleImage.builder()
                .timeCapsule(timeCapsule)
                .imgUrl(url)
                .build();
    }
}
