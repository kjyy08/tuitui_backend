package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.UserRequestDto;
import suftware.tuitui.dto.response.UserResponseDto;
import suftware.tuitui.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
        if (userRepository.existsByAccount(userRequestDto.getAccount())) {
            throw new CustomException(MsgCode.USER_EXIST);
        }

        if (!userRequestDto.getPassword().equals(userRequestDto.getCheckPassword())){
            throw new CustomException(MsgCode.USER_BAD_REQUEST);
        }

        User user = UserRequestDto.toEntity(userRequestDto);

        //  비밀번호 암호화
        String hashPassword = passwordEncoder.encode(userRequestDto.getPassword());

        //  암호화된 값과 입력받은 값이 일치하는지 확인
        if (!passwordEncoder.matches(userRequestDto.getPassword(), hashPassword)) {
            throw new CustomException(MsgCode.USER_SIGNUP_FAIL_NOT_ENCODED);
        }

        user.setPassword(hashPassword);
        user = userRepository.save(user);

        return Optional.of(UserResponseDto.toDTO(user));
    }

    @Transactional
    public Optional<UserResponseDto> updateUser(Integer id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        if (!(userRequestDto.getName() == null)) {
            user.setName(userRequestDto.getName());
        }
        if (!(userRequestDto.getPhone() == null)) {
            user.setPhone(userRequestDto.getPhone());
        }
        if (!(userRequestDto.getPassword() == null)) {
            if (!passwordEncoder.matches(userRequestDto.getCheckPassword(), user.getPassword())) {
                throw new CustomException(MsgCode.USER_BAD_REQUEST);
            }

            String hashPassword = passwordEncoder.encode(userRequestDto.getPassword());

            if (!passwordEncoder.matches(userRequestDto.getPassword(), hashPassword)) {
                throw new CustomException(MsgCode.USER_SIGNUP_FAIL_NOT_ENCODED);
            }

            user.setPassword(hashPassword);
        }

        return Optional.of(UserResponseDto.toDTO(user));
    }

    @Transactional
    public void deleteUser(UserRequestDto userRequestDto) {
        User user = userRepository.findByAccount(userRequestDto.getAccount())
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        //  재확인 비밀번호가 일치하는지 확인
        if (!userRequestDto.getPassword().equals(userRequestDto.getCheckPassword())) {
            throw new CustomException(MsgCode.USER_BAD_REQUEST);
        }
        else {
            //  복호화시킨 비밀번호와 일치하는지 확인
            if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
                throw new CustomException(MsgCode.USER_BAD_REQUEST);
            }

            userRepository.deleteByAccount(userRequestDto.getAccount());
        }
    }

    public boolean existsByAccount(String account){
        return userRepository.existsByAccount(account);
    }

    public Optional<UserResponseDto> login(UserRequestDto loginRequestDto) {
        User user = userRepository.findByAccount(loginRequestDto.getAccount())
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(MsgCode.USER_BAD_REQUEST);
        }

        return Optional.of(UserResponseDto.toDTO(user));

    }

}
