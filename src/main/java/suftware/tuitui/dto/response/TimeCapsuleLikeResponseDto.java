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
    Integer profileId;
    Integer timeCapsuleId;

    public static TimeCapsuleLikeResponseDto toDto(TimeCapsuleLike timeCapsuleLike){
        return TimeCapsuleLikeResponseDto.builder()
                .capsuleLikeId(timeCapsuleLike.getCapsuleLikeId())
                .profileId(timeCapsuleLike.getProfile().getProfileId())
                .timeCapsuleId(timeCapsuleLike.getTimeCapsule().getTimeCapsuleId())
                .build();
    }
}
