package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.Hashtag;
import suftware.tuitui.domain.TimeCapsule;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeCapsuleResponseDto {
    Integer capsuleId;

    String writeUser;

    String content;

    String location;

    Integer remindDate;

    String writeAt;

    String updateAt;

    List<ImageResponseDto> imageList;

    List<Hashtag> tagList;

    private BigDecimal latitude;

    private BigDecimal longitude;

    public static TimeCapsuleResponseDto toDTO(TimeCapsule timeCapsule) {
        return TimeCapsuleResponseDto.builder()
                .capsuleId(timeCapsule.getTimeCapsuleId())
                .writeUser(timeCapsule.getProfile().getNickname())
                .writeAt(timeCapsule.getWriteAt().toString())
                .updateAt(timeCapsule.getUpdateAt().toString())
                .content(timeCapsule.getContent())
                .location(timeCapsule.getLocation())
                .remindDate(timeCapsule.getRemindDate())
                .latitude(timeCapsule.getLatitude())
                .longitude(timeCapsule.getLongitude())
                .build();
    }

    public static TimeCapsuleResponseDto toDTO(TimeCapsule timeCapsule, List<ImageResponseDto> imageList) {
        return TimeCapsuleResponseDto.builder()
                .capsuleId(timeCapsule.getTimeCapsuleId())
                .writeUser(timeCapsule.getProfile().getNickname())
                .writeAt(timeCapsule.getWriteAt().toString())
                .updateAt(timeCapsule.getUpdateAt().toString())
                .content(timeCapsule.getContent())
                .location(timeCapsule.getLocation())
                .remindDate(timeCapsule.getRemindDate())
                .latitude(timeCapsule.getLatitude())
                .longitude(timeCapsule.getLongitude())
                .imageList(imageList)
                .build();
    }
}