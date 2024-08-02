package suftware.tuitui.dto.request;

import lombok.*;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleRequestDto {
    String writeUser;
    String content;
    String location;
    Integer remindDate;
    Timestamp writeAt;
    Timestamp updateAt;

    public static TimeCapsule toEntity(TimeCapsuleRequestDto timeCapsuleRequestDto, Profile profile) {
        return TimeCapsule.builder()
                .profile(profile)
                .writeAt(timeCapsuleRequestDto.getWriteAt())
                .updateAt(timeCapsuleRequestDto.getUpdateAt())
                .content(timeCapsuleRequestDto.getContent())
                .location(timeCapsuleRequestDto.getLocation())
                .remindDate(timeCapsuleRequestDto.getRemindDate())
                .build();
    }
}
