package suftware.tuitui.dto.response;

import lombok.*;
import suftware.tuitui.domain.TimeCapsuleVisit;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleVisitResponseDto {
    Integer capsuleId;
    Integer visitCount;

    public static TimeCapsuleVisitResponseDto toDTO(TimeCapsuleVisit timeCapsuleVisit){
        return TimeCapsuleVisitResponseDto.builder()
                .capsuleId(timeCapsuleVisit.getVisitCountId())
                .visitCount(timeCapsuleVisit.getVisitCount())
                .build();
    }
}
