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
    @Column(name = "image_id", unique = true)
    Integer imageId;

    @Column(name = "image_name", length = 200)
    String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id")
    TimeCapsule timeCapsule;

    @Column(name = "image_path", nullable = false, length = 500)
    String imagePath;
}
