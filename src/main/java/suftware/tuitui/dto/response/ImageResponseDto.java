package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.ProfileImage;
import suftware.tuitui.domain.TimeCapsuleImage;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponseDto {
    Integer imageId;
    String imagePath;

    public static ImageResponseDto toDto(TimeCapsuleImage timeCapsuleImage){
        return ImageResponseDto.builder()
                .imageId(timeCapsuleImage.getImageId())
                .imagePath(timeCapsuleImage.getImgUrl())
                .build();
    }

    public static ImageResponseDto toDto(ProfileImage profileImage){
        return ImageResponseDto.builder()
                .imageId(profileImage.getImageId())
                .imagePath(profileImage.getImgUrl())
                .build();
    }
}
