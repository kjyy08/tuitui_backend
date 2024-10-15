package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.enumType.Gender;

import java.time.LocalDate;
import java.util.List;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false, unique = true)
    Integer profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "name", nullable = false, length = 16)
    String name;

    //  사업자가 아니면 번호를 안주는 드러운 나라 api 하나 쓰기도 쉽지않네.
    @Column(name = "phone", unique = true, length = 25)
    String phone;

    @Column(name = "nickname", nullable = false, length = 45, unique = true)
    String nickname;

    @Column(name = "describe_self", length = 100)
    String describeSelf;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "birth")
    LocalDate birth;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeCapsule> timeCapsules;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeCapsuleLike> timeCapsuleLike;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHashtag> userHashtags;

    public void updatePhone(String phone){
        this.phone = phone;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateDescribeSelf(String describeSelf){
        this.describeSelf = describeSelf;
    }

    public void updateGender(Gender gender){
        this.gender = gender;
    }

    public void updateBirth(LocalDate birth){
        this.birth = birth;
    }

    public static Profile of(User user, String name, String phone, String nickname,
                             String describeSelf, Gender gender, LocalDate birth) {
        return Profile.builder()
                .user(user)
                .name(name)
                .phone(phone)
                .nickname(nickname)
                .describeSelf(describeSelf)
                .gender(gender != null ? gender : Gender.OTHER)
                .birth(birth)
                .build();
    }
}
