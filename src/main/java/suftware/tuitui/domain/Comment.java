package suftware.tuitui.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_comment_id", referencedColumnName = "capsule_comment_id")
    Comment parentComment;

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

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments;

}
