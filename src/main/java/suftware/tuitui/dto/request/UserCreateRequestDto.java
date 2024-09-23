package suftware.tuitui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
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

    //  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8~20자 영문 1자 이상, 숫자, 특수문자를 조합하여 입력해주세요.",
    //          groups = {UserValidationGroups.modify.class, UserValidationGroups.request.class})
    //  @NotEmpty(message = "비밀번호는 필수 입력 값입니다.", groups = UserValidationGroups.modify.class)
    //  String password;

    Timestamp accountCreatedDate;

    public static User toEntity(UserCreateRequestDto userCreateRequestDto){
        return User.builder()
                .account(userCreateRequestDto.getAccount())
                //  .password(userRequestDto.getPassword())
                //  .phone(userRequestDto.getPhone())
                //  .name(userRequestDto.getName())
                .createdAt(DateTimeUtil.getSeoulTimestamp())
                .role(Role.USER)
                .build();
    }

    public static User toEntity(UserCreateRequestDto userCreateRequestDto, String role){
        return User.builder()
                .account(userCreateRequestDto.getAccount())
                //  .password(userRequestDto.getPassword())
                //  .phone(userRequestDto.getPhone())
                //  .name(userRequestDto.getName())
                .createdAt(DateTimeUtil.getSeoulTimestamp())
                .role(Role.valueOf(role))
                .build();
    }
}
