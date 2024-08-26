package suftware.tuitui.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "capsule_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_comment_id", nullable = false, unique = true)
    Integer commentId;

//    @Column(name = "reference_comment_id")
//    Integer refCommentId;

    // reference_comment_id가 외래키 설정 안되어 있어서 변경합니다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_comment_id", referencedColumnName = "capsule_comment_id")
    Comment referenceCommentId;

    @Column(name = "comment", nullable = false)
    String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "profile_id")
    Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false, referencedColumnName = "capsule_id")
    TimeCapsule timeCapsule;

    @Column(name = "update_at")
    Timestamp updateAt;

    @Column(name = "modified")
    Boolean modified;
}
