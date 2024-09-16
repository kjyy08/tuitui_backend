package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.Gender;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.ProfileCreateRequestDto;
import suftware.tuitui.dto.request.ProfileUpdateRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.UserRepository;

import java.util.ArrayList;
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

        if (profileList.isEmpty()){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        List<ProfileResponseDto> profileResponseDtoList = new ArrayList<>();
        for (Profile profile : profileList){
            profileResponseDtoList.add(ProfileResponseDto.toDTO(profile));
        }

        return profileResponseDtoList;
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

        //  전화번호 중복 가입 방지
        if (profileRepository.existsByPhone(profileCreateRequestDto.getPhone())) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_PHONE);
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

                profile.setPhone(profileUpdateRequestDto.getPhone());
            }
        }

        //  닉네임 수정
        if (!(profileUpdateRequestDto.getNickname() == null)) {
            if (!profile.getNickname().equals(profileUpdateRequestDto.getNickname())) {
                if (profileRepository.existsByNickname(profileUpdateRequestDto.getNickname())) {
                    throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
                }

                profile.setNickname(profileUpdateRequestDto.getNickname());
            }
        }

        //  자기소개 수정
        if (!(profileUpdateRequestDto.getDescribeSelf() == null)) {
            if (!profile.getDescribeSelf().equals(profileUpdateRequestDto.getDescribeSelf())) {
                profile.setDescribeSelf(profileUpdateRequestDto.getDescribeSelf());
            }
        }

        //  성별 수정
        if (!(profileUpdateRequestDto.getGender() == null)) {
            if (!profile.getGender().toString().equals(profileUpdateRequestDto.getGender())) {
                profile.setGender(Gender.valueOf(profileUpdateRequestDto.getGender()));
            }
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
