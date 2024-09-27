package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.UserCreateRequestDto;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.dto.response.UserResponseDto;
import suftware.tuitui.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserResponseDto> getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    public List<UserResponseDto> getUserList() {
        List<User> userList = userRepository.findAll();

        if (userList.isEmpty()){
            throw new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND);
        }

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        for (User user : userList){
            userResponseDtoList.add(UserResponseDto.toDTO(user));
        }

        return userResponseDtoList;
    }

    public Optional<PageResponse> getUserList(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userResponseDtoList = userPage.getContent().stream()
                .map(UserResponseDto::toDTO)
                .collect(Collectors.toList());

        // PageResponse 반환
        return Optional.of(PageResponse.builder()
                .contents(userResponseDtoList)
                .pageNo(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements((int) userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build());
    }

    public Optional<UserResponseDto> getUserByAccount(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        return Optional.of(UserResponseDto.toDTO(user));
    }

    //  24.08.30 회원가입 소셜 로그인으로 대체
//    public Optional<UserResponseDto> save(UserRequestDto userRequestDto) {
//        if (userRepository.existsByAccount(userRequestDto.getAccount())) {
//            throw new CustomException(MsgCode.USER_EXIST);
//        }
//
//        User user = UserRequestDto.toEntity(userRequestDto);
//
//        //  비밀번호 암호화
//        String hashPassword = passwordEncoder.encode(userRequestDto.getPassword());
//
//        //  암호화된 값과 입력받은 값이 일치하는지 확인
//        if (!passwordEncoder.matches(userRequestDto.getPassword(), hashPassword)) {
//            throw new CustomException(MsgCode.USER_SIGNUP_FAIL_NOT_ENCODED);
//        }
//
//        user.setPassword(hashPassword);
//        user = userRepository.save(user);
//
//        return Optional.of(UserResponseDto.toDTO(user));
//    }

    //  24.08.30 회원가입 소셜 로그인으로 대체
//    @Transactional
//    public Optional<UserResponseDto> updateUser(Integer id, UserRequestDto userRequestDto) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));
//
//        if (!(userRequestDto.getPassword() == null)) {
//            String hashPassword = passwordEncoder.encode(userRequestDto.getPassword());
//
//            if (!passwordEncoder.matches(userRequestDto.getPassword(), hashPassword)) {
//                throw new CustomException(MsgCode.USER_SIGNUP_FAIL_NOT_ENCODED);
//            }
//
//            user.setPassword(hashPassword);
//        }
//
//        return Optional.of(UserResponseDto.toDTO(user));
//    }

    @Transactional
    public void deleteUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userRepository.findByUserIdAndAccount(userCreateRequestDto.getUserId(), userCreateRequestDto.getAccount())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

            //  24.08.30 회원가입을 소셜 로그인으로 대체하며 비밀번호 미사용
//        //  복호화시킨 비밀번호와 일치하는지 확인
//        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
//            throw new CustomException(MsgCode.USER_BAD_REQUEST);
//        }

        userRepository.delete(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    //  24.08.30 로그인 처리 프론트에서 소셜 로그인으로 대체
//    public Optional<UserResponseDto> login(UserRequestDto loginRequestDto) {
//        User user = userRepository.findByAccount(loginRequestDto.getAccount())
//                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));
//
//        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
//            throw new CustomException(MsgCode.USER_BAD_REQUEST);
//        }
//
//        return Optional.of(UserResponseDto.toDTO(user));
//
//    }
}
