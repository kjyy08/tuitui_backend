package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.Comment;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    Integer commentId;
    Integer parentCommentId;
    String comment;
    Integer profileId;
    String nickname;
    String profileImgPath;
    String updateAt;
    Boolean modified;
    List<Comment> childCommentList;

    public static CommentResponseDto toDTO(Comment comment){
        CommentResponseDto.CommentResponseDtoBuilder builder = CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .profileId(comment.getProfile().getProfileId())
                .nickname(comment.getProfile().getNickname())
                .profileImgPath(comment.getProfile().getProfileImage().getImgUrl())
                .updateAt(comment.getUpdateAt().toString())
                .modified(comment.getModified());

        if (comment.getParentComment() != null) {
            builder.parentCommentId(comment.getParentComment().getCommentId());
        }

        return builder.build();
    }

    public static CommentResponseDto toDTO(Comment comment, List<Comment> childCommentList){
        CommentResponseDto.CommentResponseDtoBuilder builder = CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .profileId(comment.getProfile().getProfileId())
                .nickname(comment.getProfile().getNickname())
                .profileImgPath(comment.getProfile().getProfileImage().getImgUrl())
                .updateAt(comment.getUpdateAt().toString())
                .modified(comment.getModified())
                .childCommentList(childCommentList);

        return builder.build();
    }
}
