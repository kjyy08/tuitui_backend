package suftware.tuitui.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.time.DateTimeUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "timecapsule")
public class TimeCapsule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_id", nullable = false, unique = true)
    Integer timeCapsuleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_user_id", referencedColumnName = "profile_id", nullable = false)
    @JsonIgnore
    Profile profile;
    
    @Column(name = "content")
    String content;
    
    @Column(name = "write_at", insertable = false, updatable = false)
    Timestamp writeAt;
    
    @Column(name = "update_at", insertable = false, updatable = false)
    Timestamp updateAt;
    
    @Column(name = "location", nullable = false)
    String location;
    
    @Column(name = "remind_date", nullable = false)
    Timestamp remindDate;

    @Column(name = "latitude", precision = 10, scale = 8)
    BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    BigDecimal longitude;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeCapsuleImage> timeCapsuleImages;

    @OneToOne(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeCapsuleVisit timeCapsuleVisit;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeCapsuleLike> timeCapsuleLikes;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void updateContent(String content){
        this.content = content;
        this.updateAt = DateTimeUtil.getSeoulTimestamp();
    }

    public void updateRemindDate(Integer days){
        this.remindDate = DateTimeUtil.getSeoulTimestampPlusDays(days);
        this.updateAt = DateTimeUtil.getSeoulTimestamp();
    }

    public static TimeCapsule of(Profile profile, String content, String location, Integer remindDate, BigDecimal latitude, BigDecimal longitude){
        return TimeCapsule.builder()
                .profile(profile)
                .content(content)
                .location(location)
                .remindDate(DateTimeUtil.getSeoulTimestampPlusDays(remindDate))
                .latitude(latitude)
                .longitude(longitude)
                .writeAt(DateTimeUtil.getSeoulTimestamp())
                .updateAt(DateTimeUtil.getSeoulTimestamp())
                .build();
    }
}
