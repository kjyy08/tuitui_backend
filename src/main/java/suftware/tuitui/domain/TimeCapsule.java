package suftware.tuitui.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "timecapsule")
public class TimeCapsule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_id", nullable = false)
    Integer timeCapsuleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_user_id", referencedColumnName = "profile_id", nullable = false)
    @JsonIgnore
    Profile profile;
    
    @Column(name = "content", nullable = false)
    String content;
    
    @Column(name = "write_at", insertable = false, updatable = false)
    Timestamp writeAt;
    
    @Column(name = "update_at", insertable = false, updatable = false)
    Timestamp updateAt;
    
    @Column(name = "location", nullable = false, length = 45)
    String location;
    
    @Column(name = "remind_date", nullable = false)
    Integer remindDate;

    @OneToOne(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeCapsuleVisit timeCapsuleVisit;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeCapsuleLike> timeCapsuleLikes;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
