package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.UserRequestDto;
import suftware.tuitui.dto.response.UserResponseDto;
import suftware.tuitui.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserResponseDto> getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user " + id + " Not Found"));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    public List<UserResponseDto> getUserList() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        for (User user : userList){
            userResponseDtoList.add(UserResponseDto.toDTO(user));
        }

        return userResponseDtoList;
    }

    public Optional<UserResponseDto> getUserByAccount(String account){
        Optional<User> user = userRepository.findByAccount(account);

        if (user.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(UserResponseDto.toDTO(user.get()));
        }
    }

    public Optional<UserResponseDto> save(UserRequestDto userRequestDto) {
        User user = userRepository.save(UserRequestDto.toEntity(userRequestDto));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    @Transactional
    public Optional<UserResponseDto> updateUser(UserRequestDto userRequestDto){
        Optional<User> user = userRepository.findByAccount(userRequestDto.getAccount());

        if (user.isEmpty()){
            return Optional.empty();
        }
        else {
            if (!(userRequestDto.getName() == null))
                user.get().setName(userRequestDto.getName());
            if (!(userRequestDto.getPhone() == null))
                user.get().setPhone(userRequestDto.getPhone());
            if (!(userRequestDto.getPassword() == null))
                user.get().setPassword(userRequestDto.getPassword());

            return Optional.of(UserResponseDto.toDTO(user.get()));
        }
    }

    @Transactional
    public Message deleteUser(UserRequestDto userRequestDto){
        Message message = new Message();

        if (userRepository.existsByAccount(userRequestDto.getAccount())){
            if (userRepository.existsByAccountAndPassword(userRequestDto.getAccount(), userRequestDto.getPassword())) {
                userRepository.deleteByAccount(userRequestDto.getAccount());
                message.setStatus(HttpStatus.OK.value());
                message.setMessage(MsgCode.DELETE_SUCCESS.getMsg());
                return message;
            }
            else {
                message.setStatus(HttpStatus.BAD_REQUEST.value());
                message.setMessage("비밀번호 불일치, " + MsgCode.DELETE_FAIL.getMsg());
                return message;
            }
        }
        else {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.USER_NOT_FOUND.getMsg());
            return message;
        }
    }

    public Optional<UserResponseDto> login(UserRequestDto loginRequestDto) {
        if (!userRepository.existsByAccount(loginRequestDto.getAccount())) {
            return Optional.empty();
        }
        else {
            Optional<User> user = userRepository.findByAccountAndPassword(loginRequestDto.getAccount(), loginRequestDto.getPassword());

            if (user.isEmpty()){
                return Optional.empty();
            }
            else {
                return Optional.of(UserResponseDto.toDTO(user.get()));
            }
        }
    }
}
