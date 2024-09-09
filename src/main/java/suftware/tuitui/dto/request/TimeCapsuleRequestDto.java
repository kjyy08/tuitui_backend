package suftware.tuitui.dto.request;

import jakarta.persistence.Column;
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
        TimeCapsule.TimeCapsuleBuilder builder = TimeCapsule.builder()
                .profile(profile)
                .content(timeCapsuleRequestDto.getContent())
                .location(timeCapsuleRequestDto.getLocation())
                .remindDate(timeCapsuleRequestDto.getRemindDate())
                .latitude(timeCapsuleRequestDto.getLatitude())
                .longitude(timeCapsuleRequestDto.getLongitude());

        if (timeCapsuleRequestDto.getWriteAt() == null){
            builder.writeAt(new Timestamp(System.currentTimeMillis()));
        } else {
            builder.writeAt(timeCapsuleRequestDto.getWriteAt());
        }

        if (timeCapsuleRequestDto.getUpdateAt() == null){
            builder.updateAt(new Timestamp(System.currentTimeMillis()));
        } else {
            builder.updateAt(timeCapsuleRequestDto.getUpdateAt());
        }

        return builder.build();
    }
}
