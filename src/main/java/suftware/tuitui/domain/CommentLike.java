package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "comment_like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id", nullable = false, unique = true)
    Integer commentLikeId;

    @JoinColumn(name = "comment_id", referencedColumnName = "capsule_comment_id", nullable = false)
    Comment comment;

    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id", nullable = false)
    Profile profile;
}
