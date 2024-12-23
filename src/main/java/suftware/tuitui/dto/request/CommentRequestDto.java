package suftware.tuitui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import suftware.tuitui.common.time.DateTimeUtil;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    Integer commentId;

    Integer parentCommentId;

    @NotEmpty(message = "빈 댓글은 작성이 불가합니다.")
    String comment;

    Integer profileId;

    Integer timeCapsuleId;

    //  제일 최상단 부모 댓글 엔티티 생성
    public static Comment toEntity(CommentRequestDto commentRequestDto, Profile profile, TimeCapsule timeCapsule){
        return Comment.of(commentRequestDto.getComment(), profile, timeCapsule);
    }

    //  대댓글에 해당하는 자식 댓글 엔티티 생성
    public static Comment toEntity(CommentRequestDto commentRequestDto, Comment parentComment, Profile profile, TimeCapsule timeCapsule){
        return Comment.of(parentComment, commentRequestDto.getComment(), profile, timeCapsule);
    }
}
