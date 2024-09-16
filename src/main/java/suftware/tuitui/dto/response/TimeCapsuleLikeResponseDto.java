package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.TimeCapsuleLike;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeCapsuleLikeResponseDto {
    Integer capsuleLikeId;
    Integer timeCapsuleId;
    Integer profileId;
    String nickname;
    String profileImgPath;

    public static TimeCapsuleLikeResponseDto toDto(TimeCapsuleLike timeCapsuleLike){
        return TimeCapsuleLikeResponseDto.builder()
                .capsuleLikeId(timeCapsuleLike.getCapsuleLikeId())
                .timeCapsuleId(timeCapsuleLike.getTimeCapsule().getTimeCapsuleId())
                .profileId(timeCapsuleLike.getProfile().getProfileId())
                .nickname(timeCapsuleLike.getProfile().getNickname())
                .profileImgPath(timeCapsuleLike.getProfile().getProfileImage().getImgUrl())
                .build();
    }
}
