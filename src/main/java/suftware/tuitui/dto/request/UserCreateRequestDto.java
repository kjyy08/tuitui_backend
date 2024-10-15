package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import suftware.tuitui.common.enumType.AccountState;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.common.time.DateTimeUtil;
import suftware.tuitui.domain.User;

import java.sql.Timestamp;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDto {
    Integer userId;

    @Email(message = "아이디는 이메일 형식으로 입력해야 합니다.")
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    String account;

    Timestamp accountCreatedDate;
}
