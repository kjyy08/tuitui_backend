package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.Gender;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.ProfileCreateRequestDto;
import suftware.tuitui.dto.request.ProfileUpdateRequestDto;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;


    public Optional<ProfileResponseDto> getProfile(Integer id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public Optional<ProfileResponseDto> getProfileByNickname(String nickname) {
        Profile profile = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public Optional<ProfileResponseDto> getProfileByUserId(Integer id) {
        Profile profile = profileRepository.findByUser_UserId(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public List<ProfileResponseDto> getProfileList() {
        List<Profile> profileList = profileRepository.findAll();

        if (profileList.isEmpty()) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        return profileList.stream()
                .map(ProfileResponseDto::toDTO)
                .toList();
    }


    public Optional<PageResponse> getProfileList(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Profile> profilePage = profileRepository.findAll(pageable);

        List<ProfileResponseDto> profileResponseDtoList = profilePage.getContent().stream()
                .map(ProfileResponseDto::toDTO)
                .toList();

        // PageResponse 반환
        return Optional.of(PageResponse.builder()
                .contents(profileResponseDtoList)
                .pageNo(profilePage.getNumber())
                .pageSize(profilePage.getSize())
                .totalElements((int) profilePage.getTotalElements())
                .totalPages(profilePage.getTotalPages())
                .lastPage(profilePage.isLast())
                .build());
    }

    //  프로필 저장
    public Optional<ProfileResponseDto> saveProfile(ProfileCreateRequestDto profileCreateRequestDto) {
        //  유저가 존재하는지 확인
        User user = userRepository.findById(profileCreateRequestDto.getUserId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        //  해당 유저의 프로필이 존재하는지 확인
        if (profileRepository.existsByUser_UserId(profileCreateRequestDto.getUserId())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST);
        }

        //  닉네임 중복 확인
        if (profileRepository.existsByNickname(profileCreateRequestDto.getNickname())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
        }

        Profile profile = profileRepository.save(ProfileCreateRequestDto.toEntity(profileCreateRequestDto, user));
        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  프로필 업데이트
    @Transactional
    public Optional<ProfileResponseDto> updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        Profile profile = profileRepository.findById(profileUpdateRequestDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        //  DB와 값이 다르다면 업데이트 처리
        //  전화번호 수정
        if (!(profileUpdateRequestDto.getPhone() == null)) {
            if (!profile.getPhone().equals(profileUpdateRequestDto.getPhone())) {
                if (profileRepository.existsByPhone(profileUpdateRequestDto.getPhone())) {
                    throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_PHONE);
                }

                profile.updatePhone(profileUpdateRequestDto.getPhone());
            }
        }

        //  닉네임 수정
        if (!(profileUpdateRequestDto.getNickname() == null)) {
            if (!profile.getNickname().equals(profileUpdateRequestDto.getNickname())) {
                if (profileRepository.existsByNickname(profileUpdateRequestDto.getNickname())) {
                    throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
                }

                profile.updateNickname(profileUpdateRequestDto.getNickname());
            }
        }

        //  자기소개 수정
        if (!(profileUpdateRequestDto.getDescribeSelf() == null)) {
            if (!profile.getDescribeSelf().equals(profileUpdateRequestDto.getDescribeSelf())) {
                profile.updateDescribeSelf(profileUpdateRequestDto.getDescribeSelf());
            }
        }

        //  성별 수정
        if (!(profileUpdateRequestDto.getGender() == null)) {
            if (!profile.getGender().toString().equals(profileUpdateRequestDto.getGender())) {
                profile.updateGender(Gender.valueOf(profileUpdateRequestDto.getGender()));
            }
        }

        //  생일 수정
        if (!(profileUpdateRequestDto.getBirth() == null)) {
            profile.updateBirth(profileUpdateRequestDto.getBirth());
        }

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  프로필 삭제
    @Transactional
    public void deleteProfile(Integer profileId){
        if (!profileRepository.existsById(profileId)) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }
        else {
            profileRepository.deleteById(profileId);
        }
    }
}
