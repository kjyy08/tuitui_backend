package suftware.tuitui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import suftware.tuitui.common.enumType.Gender;
import suftware.tuitui.common.valid.ProfileValidationGroups;
import suftware.tuitui.common.valid.UserValidationGroups;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {
    Integer userId;

    @Pattern(regexp = "(^[가-힣]{2,}$)|(^[a-zA-Z]{6,}$)", message = "이름은 한글로 2자 이상 또는 영어로 6자 이상 작성해야 합니다.", groups = ProfileValidationGroups.class)
    @NotEmpty(message = "이름은 필수 입력 값입니다.", groups = ProfileValidationGroups.create.class)
    String name;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{3,4}$", message = "전화번호 형식에 맞게 입력해주세요.")
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.", groups = ProfileValidationGroups.create.class)
    String phone;

    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,20}$", message = "닉네임은 한글, 영어, 숫자 조합으로 2자 이상 20자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임은 필수 입력 값입니다.", groups = ProfileValidationGroups.create.class)
    String nickname;

    @Size(max = 100, message = "자기소개는 100자 이하로 입력해주세요.")
    String describeSelf;

    @NotEmpty(message = "성별은 필수 입력 값입니다.", groups = ProfileValidationGroups.create.class)
    String gender;

    LocalDate birth;

    byte[] profileImg;

    public static Profile toEntity(ProfileRequestDto profileRequestDto, User user, String profileImgPath){
        return Profile.builder()
                .user(user)
                .name(profileRequestDto.getName())
                .phone(profileRequestDto.getPhone())
                .nickname(profileRequestDto.getNickname())
                .describeSelf(profileRequestDto.getDescribeSelf())
                .gender(Gender.valueOf(profileRequestDto.getGender()))
                .birth(profileRequestDto.getBirth())
                .profileImgPath(profileImgPath)
                .build();
    }
}
