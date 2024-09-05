package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.Gender;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.ProfileRequestDto;
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

    @Value("${cloud.aws.cloud-front}")
    private String hostUrl;
    private final String directoryPath = "profile_image/";
    private final String basicProfileImgPath = "basic_profilie_image.png";

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
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

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

    //  파일 포함 프로필 저장
    public Optional<ProfileResponseDto> saveProfile(ProfileRequestDto profileRequestDto, MultipartFile file) {
        //  유저가 존재하는지 확인
        User user = userRepository.findById(profileRequestDto.getUserId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        //  해당 유저의 프로필이 존재하는지 확인
        if (profileRepository.existsByUser_UserId(profileRequestDto.getUserId())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST);
        }
        //  닉네임 중복 확인
        else if (profileRepository.existsByNickname(profileRequestDto.getNickname())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
        }

        String filePath = hostUrl + directoryPath + file.getOriginalFilename();
        Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user, filePath));
        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  파일 미포함 프로필 저장
    public Optional<ProfileResponseDto> saveProfile(ProfileRequestDto profileRequestDto) {
        //  유저가 존재하는지 확인
        User user = userRepository.findById(profileRequestDto.getUserId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        //  해당 유저의 프로필이 존재하는지 확인
        if (profileRepository.existsByUser_UserId(profileRequestDto.getUserId())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST);
        }

        //  전화번호 중복 가입 방지
        if (profileRepository.existsByPhone(profileRequestDto.getPhone())) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_PHONE);
        }

        //  닉네임 중복 확인
        else if (profileRepository.existsByNickname(profileRequestDto.getNickname())){
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
        }

        String filePath = hostUrl + directoryPath + basicProfileImgPath;
        Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user, filePath));
        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  프로필 업데이트
    @Transactional
    public Optional<ProfileResponseDto> updateProfile(ProfileRequestDto profileRequestDto) {
        Profile profile = profileRepository.findByUser_UserId(profileRequestDto.getUserId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        //  DB와 값이 다르다면 업데이트 처리
        //  전화번호 수정
        if (!(profileRequestDto.getPhone() == null)) {
            if (!profile.getPhone().equals(profileRequestDto.getPhone())) {
                if (profileRepository.existsByPhone(profileRequestDto.getPhone())) {
                    throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_PHONE);
                }

                profile.setPhone(profileRequestDto.getPhone());
            }
        }

        //  닉네임 수정
        if (!(profileRequestDto.getNickname() == null)) {
            if (!profile.getNickname().equals(profileRequestDto.getNickname())) {
                if (profileRepository.existsByNickname(profileRequestDto.getNickname())) {
                    throw new TuiTuiException(TuiTuiMsgCode.PROFILE_EXIST_NICKNAME);
                }

                profile.setNickname(profileRequestDto.getNickname());
            }
        }

        //  자기소개 수정
        if (!(profileRequestDto.getDescribeSelf() == null)) {
            if (!profile.getDescribeSelf().equals(profileRequestDto.getDescribeSelf())) {
                profile.setDescribeSelf(profileRequestDto.getDescribeSelf());
            }
        }

        //  성별 수정
        if (!(profileRequestDto.getGender() == null)) {
            if (!profile.getGender().toString().equals(profileRequestDto.getGender())) {
                profile.setGender(Gender.valueOf(profileRequestDto.getGender()));
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
