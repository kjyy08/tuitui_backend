package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.Image;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponseDto {
    String imageName;
    //  Integer timeCapsuleId;
    String imagePath;

    public static ImageResponseDto toDto(Image image){
        ImageResponseDtoBuilder builder = ImageResponseDto.builder()
                .imageName(image.getImageName())
                //  .timeCapsuleId(image.getTimeCapsule().getTimeCapsuleId())
                .imagePath(image.getImagePath());

        return builder.build();
    }
}
