package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
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

    Integer profileId;

    String nickname;

    String profileImgPath;

    String content;

    String location;

    Integer remindDate;

    String writeAt;

    String updateAt;

    List<ImageResponseDto> imageList;

    private BigDecimal latitude;

    private BigDecimal longitude;

    public static TimeCapsuleResponseDto toDTO(TimeCapsule timeCapsule) {
        return TimeCapsuleResponseDto.builder()
                .capsuleId(timeCapsule.getTimeCapsuleId())
                .profileId(timeCapsule.getProfile().getProfileId())
                .nickname(timeCapsule.getProfile().getNickname())
                .profileImgPath(timeCapsule.getProfile().getProfileImage().getImgUrl())
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
                .profileId(timeCapsule.getProfile().getProfileId())
                .nickname(timeCapsule.getProfile().getNickname())
                .profileImgPath(timeCapsule.getProfile().getProfileImage().getImgUrl())
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