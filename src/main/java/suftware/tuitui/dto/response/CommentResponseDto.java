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
    String nickname;
    String updateAt;
    Boolean modified;
    List<Comment> childCommentList;

    public static CommentResponseDto toDTO(Comment comment){
        CommentResponseDto.CommentResponseDtoBuilder builder = CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .nickname(comment.getProfile().getNickname())
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
                .nickname(comment.getProfile().getNickname())
                .updateAt(comment.getUpdateAt().toString())
                .modified(comment.getModified())
                .childCommentList(childCommentList);

        return builder.build();
    }
}
