package suftware.tuitui.dto.request;

import lombok.*;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleRequestDto {
    private Integer capsuleId;
    private Integer profileId;
    private String content;
    private String location;
    private Integer remindDate;
    private Timestamp writeAt;
    private Timestamp updateAt;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static TimeCapsule toEntity(TimeCapsuleRequestDto timeCapsuleRequestDto, Profile profile) {
        return TimeCapsule.of(profile,
                timeCapsuleRequestDto.getContent(),
                timeCapsuleRequestDto.getLocation(),
                timeCapsuleRequestDto.getRemindDate(),
                timeCapsuleRequestDto.getLatitude(),
                timeCapsuleRequestDto.getLongitude());
    }
}
