package suftware.tuitui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import suftware.tuitui.common.enumType.Gender;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreateRequestDto {
    Integer userId;

    Integer profileId;

    @Pattern(regexp = "(^[가-힣]{2,}$)|(^[a-zA-Z]{6,}$)", message = "이름은 한글로 2자 이상 또는 영어로 6자 이상 작성해야 합니다.")
    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    String name;

    //  @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    String phone;

    @Pattern(regexp = "^[a-zA-Z0-9_.]{2,15}$", message = "닉네임은 영어, 숫자, '_', '.' 조합으로 2자 이상 15자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    String nickname;

    @Size(max = 100, message = "자기소개는 100자 이하로 입력해주세요.")
    String describeSelf;

    String gender;

    LocalDate birth;

    byte[] profileImg;

    public static Profile toEntity(ProfileCreateRequestDto profileCreateRequestDto, User user){
        return Profile.of(
                user,
                profileCreateRequestDto.getName(),
                profileCreateRequestDto.getPhone(),
                profileCreateRequestDto.getNickname(),
                profileCreateRequestDto.getDescribeSelf(),
                Gender.valueOf(profileCreateRequestDto.getGender()),
                profileCreateRequestDto.getBirth());
    }
}
