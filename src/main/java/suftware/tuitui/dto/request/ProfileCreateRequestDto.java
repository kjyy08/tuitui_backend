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

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
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
        Profile.ProfileBuilder builder = Profile.builder()
                .user(user)
                .name(profileCreateRequestDto.getName())
                .phone(profileCreateRequestDto.getPhone())
                .nickname(profileCreateRequestDto.getNickname())
                .describeSelf(profileCreateRequestDto.getDescribeSelf());

        // gender 값이 null이면 OTHER로 저장
        if (profileCreateRequestDto.getGender() == null) {
            builder.gender(Gender.OTHER);
        } else {
            builder.gender(Gender.valueOf(profileCreateRequestDto.getGender()));
        }

        // birth 값이 null이 아닌 경우에만 저장
        if (profileCreateRequestDto.getBirth() != null) {
            builder.birth(profileCreateRequestDto.getBirth());
        }

        return builder.build();
    }
}
