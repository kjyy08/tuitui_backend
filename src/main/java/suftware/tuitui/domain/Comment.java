package suftware.tuitui.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import suftware.tuitui.common.time.DateTimeUtil;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "capsule_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_comment_id", nullable = false, unique = true)
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_comment_id", referencedColumnName = "capsule_comment_id")
    private Comment parentComment;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false, referencedColumnName = "capsule_id")
    private TimeCapsule timeCapsule;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "modified")
    private Boolean modified;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments;

    public void updateComment(String comment){
        this.comment = comment;
        this.updateAt = DateTimeUtil.getSeoulTimestamp();
        this.modified = true;
    }

    public static Comment of(Comment parentComment, String comment, Profile profile, TimeCapsule timeCapsule) {
        return Comment.builder()
                .parentComment(parentComment)
                .comment(comment)
                .profile(profile)
                .timeCapsule(timeCapsule)
                .updateAt(DateTimeUtil.getSeoulTimestamp())
                .modified(false)
                .build();
    }

    public static Comment of(String comment, Profile profile, TimeCapsule timeCapsule) {
        return Comment.builder()
                .comment(comment)
                .profile(profile)
                .timeCapsule(timeCapsule)
                .updateAt(DateTimeUtil.getSeoulTimestamp())
                .modified(false)
                .build();
    }
}
