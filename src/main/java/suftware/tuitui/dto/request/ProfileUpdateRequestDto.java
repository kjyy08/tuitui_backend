package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    Integer profileId;

    String phone;

    @Pattern(regexp = "^[a-zA-Z0-9_.]{2,15}$", message = "닉네임은 영어, 숫자, '_', '.' 조합으로 2자 이상 15자 이하로 입력해주세요.")
    String nickname;

    @Size(max = 100, message = "자기소개는 100자 이하로 입력해주세요.")
    String describeSelf;

    String gender;

    LocalDate birth;

    byte[] profileImg;
}
