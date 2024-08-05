package suftware.tuitui.dto.request;

import lombok.Getter;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.TimeCapsule;

@Getter
public class ImageRequestDto {
    String imageName;
    Integer timeCapsuleId;

    public static Image toEntity(ImageRequestDto imageRequestDto, TimeCapsule timeCapsule, String imagePath){
        return Image.builder()
                .imageName(imageRequestDto.getImageName())
                .timeCapsule(timeCapsule)
                .imagePath(imagePath)
                .build();
    }
}