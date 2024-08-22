package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import suftware.tuitui.common.valid.UserValidationGroups;
import suftware.tuitui.domain.User;

import java.sql.Timestamp;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Email(message = "아이디는 이메일 형식으로 입력해야 합니다.")
    @NotEmpty(message = "아이디는 필수 입력 값입니다.", groups = UserValidationGroups.signUp.class)
    String account;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8~20자 영문 1자 이상, 숫자, 특수문자를 조합하여 입력해주세요.")
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.", groups = UserValidationGroups.signUp.class)
    String password;

    @NotEmpty(message = "비밀번호 확인을 위해 필수로 입력해주세요.", groups = UserValidationGroups.signUp.class)
    String checkPassword;

    Timestamp accountCreatedDate;

    public static User toEntity(UserRequestDto userRequestDto){
        return User.builder()
                .account(userRequestDto.getAccount())
                .password(userRequestDto.getPassword())
                //  .phone(userRequestDto.getPhone())
                //  .name(userRequestDto.getName())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
