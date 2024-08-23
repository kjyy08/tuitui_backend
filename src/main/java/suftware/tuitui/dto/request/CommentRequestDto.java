package suftware.tuitui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    Integer refCommentId;

    @NotEmpty(message = "빈 댓글은 작성이 불가합니다.")
    String comment;

    Integer profileId;

    Integer timeCapsuleId;

    public static Comment toEntity(CommentRequestDto commentRequestDto, Profile profile, TimeCapsule timeCapsule){
        return Comment.builder()
                .refCommentId(commentRequestDto.getRefCommentId())
                .comment(commentRequestDto.getComment())
                .profile(profile)
                .timeCapsule(timeCapsule)
                .updateAt(new Timestamp(System.currentTimeMillis()))
                .modified(Boolean.FALSE)
                .build();
    }
}
