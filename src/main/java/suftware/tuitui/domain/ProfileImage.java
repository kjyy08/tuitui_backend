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
@Table(name = "profile_image")
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id", nullable = false, unique = true)
    Integer imageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id", nullable = false, unique = true)
    Profile profile;

    @Column(name = "profile_image_path", nullable = false)
    String imgUrl;

    public void updateImgUrl(String url){
        this.imgUrl = url;
    }

    public static ProfileImage of(Profile profile, String url) {
        return ProfileImage.builder()
                .profile(profile)
                .imgUrl(url)
                .build();
    }
}
