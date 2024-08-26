package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.CommentLike;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeResponseDto {
    Integer commentLikeId;
    Integer profileId;
    Integer commentId;
//    String nickName; // 이거 넣을까 말까

    public static CommentLikeResponseDto toDto(CommentLike commentLike){
        return CommentLikeResponseDto.builder()
                .commentLikeId(commentLike.getCommentLikeId())
                .commentId(commentLike.getComment().getCommentId())
                .profileId(commentLike.getProfile().getProfileId())
//                .nickName(commentLike.getProfile().getNickname()) // 필요 없을거 같아서 뺌요
                .build();
    }
}
