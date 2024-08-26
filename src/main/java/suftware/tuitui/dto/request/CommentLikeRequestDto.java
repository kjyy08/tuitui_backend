package suftware.tuitui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.CommentLike;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeRequestDto {
    Integer profileId;
    Integer commentId;

    public static CommentLike toEntity(Comment comment, Profile profile){
        return CommentLike.builder()
                .comment(comment)
                .profile(profile)
                .build();
    }

}
