package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import suftware.tuitui.domain.Comment;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    Integer commentId;
    Integer refCommentId;
    String comment;
    String nickname;
    String writeAt;
    Boolean modified;

    public static CommentResponseDto toDTO(Comment comment){
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .refCommentId(comment.getRefCommentId())
                .comment(comment.getComment())
                .nickname(comment.getProfile().getNickname())
                .writeAt(comment.getWriteAt().toString())
                .modified(comment.getModified())
                .build();
    }
}
