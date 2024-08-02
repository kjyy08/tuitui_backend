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
    @Column(name = "comment_id", unique = true)
    Integer commentId;

    @Column(name = "reference_comment_id")
    Integer refCommentId;

    @Column(name = "comment")
    String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "profile_id")
    Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id")
    TimeCapsule timeCapsule;

    @Column(name = "write_at")
    Timestamp writeAt;

    @Column(name = "modified")
    Boolean modified;
}
