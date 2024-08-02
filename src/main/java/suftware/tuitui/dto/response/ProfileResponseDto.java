package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;

import java.time.LocalDate;
import java.util.Base64;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {
    Integer profileId;
    Integer userId;
    String nickname;
    String describeSelf;
    String gender;
    LocalDate birth;
    String profileImgPath;

    public static ProfileResponseDto toDTO(Profile profile){
        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .userId(profile.getUser().getUserId())
                .nickname(profile.getNickname())
                .gender(profile.getGender())
                .describeSelf(profile.getDescribeSelf())
                .birth(profile.getBirth())
                .profileImgPath(profile.getProfileImgPath());

        return builder.build();
    }
}
