package suftware.tuitui.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.dto.request.UserRequestDto;
import suftware.tuitui.dto.response.*;
import suftware.tuitui.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class UserController {
    private UserService userService;

    //  전체 유저 조회
    @GetMapping(value = "users")
    public ResponseEntity<Message> getUserList() {
        Message message = new Message();
        List<UserResponseDto> userResponseDtoList = userService.getUserList();

        if (userResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(userResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  유저 id 기준 조회
    @GetMapping(value = "users/{userId}")
    public ResponseEntity<Message> getUser(@PathVariable(name = "userId") Integer user_id) {
        Message message = new Message();
        Optional<UserResponseDto> userResponseDto = userService.getUser(user_id);

        if (userResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(userResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  유저 생성
    @PostMapping(value = "users")
    public ResponseEntity<Message> saveUser(@RequestBody @Valid UserRequestDto userRequestDto, Errors errors){
        Optional<UserResponseDto> userResponseDto = Optional.of(new UserResponseDto());
        Message message = new Message();
        userResponseDto = userService.getUserByAccount(userRequestDto.getAccount());

        if (userResponseDto.isPresent()){
            message.setStatus(HttpStatus.CONFLICT.value());
            message.setMessage(MsgCode.USER_EXIST.getMsg());
            message.setData(userResponseDto);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(message);
        }

        userResponseDto = userService.save(userRequestDto);
        //  유저 생성에 실패함
        if (userResponseDto.isEmpty()){
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.SIGNUP_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        //  유저 생성 성공
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.SIGNUP_SUCCESS.getMsg());
            message.setData(userResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  유저 업데이트
    @PutMapping(value = "users")
    public ResponseEntity<Message> updateUser(@RequestBody UserRequestDto userRequestDto){
        Message message = new Message();
        Optional<UserResponseDto> userResponseDto = userService.updateUser(userRequestDto);

        if (userResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.USER_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.UPDATE_SUCCESS.getMsg());
            message.setData(userResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  유저 삭제
    @DeleteMapping(value = "users")
    public ResponseEntity<Message> deleteUser(@RequestBody UserRequestDto userRequestDto){
        Message message = userService.deleteUser(userRequestDto);

        return ResponseEntity.status(message.getStatus())
                .body(message);
    }

    //  유저 로그인
    @PostMapping(value = "login")
    public ResponseEntity<Message> loginUser(@RequestBody UserRequestDto userRequestDto){
        Optional<UserResponseDto> userResponseDto = userService.login(userRequestDto);
        Message message = new Message();

        if (userResponseDto.isEmpty()){
            message.setStatus(HttpStatus.UNAUTHORIZED.value());
            message.setMessage(MsgCode.LOGIN_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.LOGIN_SUCCESS.getMsg());
            message.setData(userResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }
}
