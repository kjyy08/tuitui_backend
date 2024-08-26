package suftware.tuitui.dto.request;

import lombok.Getter;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.TimeCapsule;

@Getter
public class ImageRequestDto {
//    String imageName;     //  일단 필요 없어서 주석처리함
    Integer timeCapsuleId;

    public Integer getTimeCapsuleId() {
        return timeCapsuleId;
    }

    public static Image toEntity(String imageName, TimeCapsule timeCapsule, String imagePath){
        return Image.builder()
                .imageName(imageName)
                .timeCapsule(timeCapsule)
                .imagePath(imagePath)
                .build();
    }

//    public void setTimeCapsuleId(Integer timeCapsuleId) {
//        this.timeCapsuleId = timeCapsuleId;
//    }
}