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
import suftware.tuitui.dto.request.UserUpdateRequestDto;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.dto.response.UserResponseDto;
import suftware.tuitui.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

        return userList.stream()
                .map(UserResponseDto::toDTO)
                .toList();
    }

    public Optional<PageResponse> getUserList(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userResponseDtoList = userPage.getContent().stream()
                .map(UserResponseDto::toDTO)
                .toList();

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

    @Transactional
    public void deleteUser(UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByUserIdAndAccount(userUpdateRequestDto.getUserId(), userUpdateRequestDto.getAccount())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}
