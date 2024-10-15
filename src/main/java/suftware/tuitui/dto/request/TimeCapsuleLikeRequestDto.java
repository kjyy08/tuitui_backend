package suftware.tuitui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleLike;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleLikeRequestDto {
    Integer profileId;
    Integer timeCapsuleId;

    public static TimeCapsuleLike toEntity(TimeCapsule timeCapsule, Profile profile){
        return TimeCapsuleLike.of(timeCapsule, profile);
    }
}
