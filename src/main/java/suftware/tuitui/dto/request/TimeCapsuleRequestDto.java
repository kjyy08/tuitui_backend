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
        TimeCapsule.TimeCapsuleBuilder builder = TimeCapsule.builder()
                .profile(profile)
                .content(timeCapsuleRequestDto.getContent())
                .location(timeCapsuleRequestDto.getLocation())
                .remindDate(timeCapsuleRequestDto.getRemindDate());

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
