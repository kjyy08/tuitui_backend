package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    Integer userId;

    @Email(message = "아이디는 이메일 형식으로 입력해야 합니다.")
    String account;

    //  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8~20자 영문 1자 이상, 숫자, 특수문자를 조합하여 입력해주세요.")
    //  String password;

    Timestamp accountCreatedDate;
}
