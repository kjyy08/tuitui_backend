package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false, unique = true)
    Integer imageId;

    @Column(name = "image_name", nullable = false, length = 200)
    String imageName;

    @Column(name = "image_path", nullable = false, length = 2083)
    String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false, referencedColumnName = "capsule_id")
    TimeCapsule timeCapsule;
}
