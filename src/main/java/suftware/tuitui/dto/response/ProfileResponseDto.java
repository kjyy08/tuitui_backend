package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.Profile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {
    Integer profileId;
    Integer userId;
    String name;
    String phone;
    String nickname;
    String describeSelf;
    String gender;
    LocalDate birth;
    String profileImgPath;

    public static ProfileResponseDto toDTO(Profile profile){
        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .userId(profile.getUser().getUserId())
                .name(profile.getName())
                .phone(profile.getPhone())
                .nickname(profile.getNickname())
                .gender(profile.getGender().toString())
                .describeSelf(profile.getDescribeSelf())
                .birth(profile.getBirth());

        return builder.build();
    }

    public static ProfileResponseDto toDTO(Profile profile, String imgPath){
        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .userId(profile.getUser().getUserId())
                .name(profile.getName())
                .phone(profile.getPhone())
                .nickname(profile.getNickname())
                .gender(profile.getGender().toString())
                .describeSelf(profile.getDescribeSelf())
                .birth(profile.getBirth())
                .profileImgPath(imgPath);

        return builder.build();
    }
}
