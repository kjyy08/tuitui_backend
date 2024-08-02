package suftware.tuitui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {
    Integer userId;

    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    String nickname;

    String describeSelf;

    String gender;

    LocalDate birth;

    byte[] profileImg;

    public static Profile toEntity(ProfileRequestDto profileRequestDto, User user, String profileImgPath){
        return Profile.builder()
                .user(user)
                .nickname(profileRequestDto.getNickname())
                .describeSelf(profileRequestDto.getDescribeSelf())
                .gender(profileRequestDto.getGender())
                .birth(profileRequestDto.getBirth())
                .profileImgPath(profileImgPath)
                .build();
    }
}
