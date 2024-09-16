package suftware.tuitui.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.UserCreateRequestDto;
import suftware.tuitui.dto.response.*;
import suftware.tuitui.service.UserService;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class UserController {
    private final UserService userService;

    //  전체 유저 조회
    @GetMapping(value = "users")
    public ResponseEntity<Message> readUserList() {
        List<UserResponseDto> userResponseDtoList = userService.getUserList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.USER_READ_SUCCESS.getMsg())
                .data(userResponseDtoList)
                .build());
    }

    //  유저 id 기준 조회
    @GetMapping(value = "users/{userId}")
    public ResponseEntity<Message> readUser(@PathVariable(name = "userId") Integer user_id) {
        Optional<UserResponseDto> userResponseDto = userService.getUser(user_id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.USER_READ_SUCCESS.getMsg())
                .data(userResponseDto)
                .build());
    }

//    //  유저 생성
//    @PostMapping(value = "signup")
//    public ResponseEntity<Message> createUser(@RequestBody @Validated({UserValidationGroups.modify.class, UserValidationGroups.request.class}) UserRequestDto userRequestDto,
//                                              BindingResult bindingResult) {
//        if (bindingResult.hasErrors()){
//            throw new CustomException(MsgCode.USER_SIGNUP_FAIL, getValidatorResult(bindingResult));
//        }
//
//        Optional<UserResponseDto> userResponseDto = userService.save(userRequestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
//                .status(HttpStatus.CREATED)
//                .message(MsgCode.USER_SIGNUP_SUCCESS.getMsg())
//                .data(userResponseDto)
//                .build());
//    }

//    //  유저 업데이트
//    @PutMapping(value = "users/{userId}")
//    public ResponseEntity<Message> updateUser(@PathVariable("userId") Integer id,
//                                              @RequestBody @Validated(UserValidationGroups.request.class) UserRequestDto userRequestDto,
//                                              BindingResult bindingResult) {
//        if (bindingResult.hasErrors()){
//            throw new CustomException(MsgCode.USER_NOT_VALID, getValidatorResult(bindingResult));
//        }
//
//        Optional<UserResponseDto> userResponseDto = userService.updateUser(id, userRequestDto);
//
//        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
//                .status(HttpStatus.OK)
//                .message(MsgCode.USER_UPDATE_SUCCESS.getMsg())
//                .data(userResponseDto)
//                .build());
//    }

    //  유저 삭제
    @DeleteMapping(value = "users")
    public ResponseEntity<Message> deleteUser(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto){
        userService.deleteUser(userCreateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.USER_DELETE_SUCCESS.getMsg())
                .build());
    }

    //  유저 로그인
    //  @PostMapping(value = "login")
    //  public String loginUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
    //      //Optional<UserResponseDto> userResponseDto = userService.login(userRequestDto);
    //
    //      return "ok";
    //      //  return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
    //      //          .status(HttpStatus.OK)
    //      //          //.message(MsgCode.USER_LOGIN_SUCCESS.getMsg())
    //      //          //.data(userResponseDto)
    //      //          .build());
    //  }

}
