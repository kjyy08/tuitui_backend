package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
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
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    public List<UserResponseDto> getUserList() {
        List<User> userList = userRepository.findAll();

        if (userList.isEmpty()){
            throw new CustomException(MsgCode.USER_NOT_FOUND);
        }

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        for (User user : userList){
            userResponseDtoList.add(UserResponseDto.toDTO(user));
        }

        return userResponseDtoList;
    }

    public Optional<UserResponseDto> getUserByAccount(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    public Optional<UserResponseDto> save(UserRequestDto userRequestDto) {
        if (userRepository.existsByAccount(userRequestDto.getAccount())){
            throw new CustomException(MsgCode.USER_EXIST);
        }

        User user = userRepository.save(UserRequestDto.toEntity(userRequestDto));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    @Transactional
    public Optional<UserResponseDto> updateUser(UserRequestDto userRequestDto) {
        User user = userRepository.findByAccount(userRequestDto.getAccount())
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        if (!(userRequestDto.getName() == null))
            user.setName(userRequestDto.getName());
        if (!(userRequestDto.getPhone() == null))
            user.setPhone(userRequestDto.getPhone());
        if (!(userRequestDto.getPassword() == null))
            user.setPassword(userRequestDto.getPassword());

        return Optional.of(UserResponseDto.toDTO(user));
    }

    @Transactional
    public void deleteUser(UserRequestDto userRequestDto){
        if (userRepository.existsByAccount(userRequestDto.getAccount())){
            if (!userRepository.existsByAccountAndPassword(userRequestDto.getAccount(), userRequestDto.getPassword())) {
                throw new CustomException(MsgCode.USER_BAD_REQUEST);
            }
            else {
                userRepository.deleteByAccount(userRequestDto.getAccount());
            }
        }
        else {
            throw new CustomException(MsgCode.USER_NOT_FOUND);
        }
    }

    public Optional<UserResponseDto> login(UserRequestDto loginRequestDto) {
        if (!userRepository.existsByAccount(loginRequestDto.getAccount())) {
            throw new CustomException(MsgCode.USER_NOT_FOUND);
        }
        else {
            User user = userRepository.findByAccountAndPassword(loginRequestDto.getAccount(), loginRequestDto.getPassword())
                    .orElseThrow(() -> new CustomException(MsgCode.USER_BAD_REQUEST));

            return Optional.of(UserResponseDto.toDTO(user));
        }
    }
}
