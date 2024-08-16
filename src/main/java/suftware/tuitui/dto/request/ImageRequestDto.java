package suftware.tuitui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.TimeCapsule;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequestDto {
//    String imageName;     //  일단 필요 없어서 주석처리함
    Integer timeCapsuleId;

    public static Image toEntity(String imageName, TimeCapsule timeCapsule, String imagePath){
        return Image.builder()
                .imageName(imageName)
                .timeCapsule(timeCapsule)
                .imagePath(imagePath)
                .build();
    }
}