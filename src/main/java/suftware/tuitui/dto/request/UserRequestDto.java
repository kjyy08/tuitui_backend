package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import suftware.tuitui.domain.User;

import java.sql.Timestamp;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Email(message = "아이디는 이메일 형식으로 입력해야 합니다.")
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    String account;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    String password;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식에 맞게 입력해주세요.")
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    String phone;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    String name;

    Timestamp accountCreatedDate;

    public static User toEntity(UserRequestDto userRequestDto){
        return User.builder()
                .account(userRequestDto.getAccount())
                .password(userRequestDto.getPassword())
                .phone(userRequestDto.getPhone())
                .name(userRequestDto.getName())
                .accountCreatedDate(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
