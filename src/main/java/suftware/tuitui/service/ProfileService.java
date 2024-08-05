package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
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
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.cloud-front}")
    private String hostUrl;
    private final String directoryPath = "profile_image/";
    private final String basicProfileImgPath = "basic_profilie_image.png";

    public Optional<ProfileResponseDto> getProfile(Integer id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public Optional<ProfileResponseDto> getProfileByNickname(String nickname) {
        Profile profile = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public Optional<ProfileResponseDto> getProfileByUserId(Integer id) {
        Profile profile = profileRepository.findByUser_UserId(id)
                .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    public List<ProfileResponseDto> getProfileList() {
        List<Profile> profileList = profileRepository.findAll();

        if (profileList.isEmpty()){
            throw new CustomException(MsgCode.PROFILE_NOT_FOUND);
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
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        //  해당 유저의 프로필이 존재하는지 확인
        if (profileRepository.existsByUser_UserId(profileRequestDto.getUserId())){
            throw new CustomException(MsgCode.PROFILE_EXIT);
        }

        String filePath = hostUrl + directoryPath + file.getOriginalFilename();
        Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user, filePath));
        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  파일 미포함 프로필 저장
    public Optional<ProfileResponseDto> saveProfile(ProfileRequestDto profileRequestDto) {
        User user = userRepository.findById(profileRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        String filePath = hostUrl + directoryPath + basicProfileImgPath;
        Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user, filePath));
        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    @Transactional
    public Optional<ProfileResponseDto> updateProfile(ProfileRequestDto profileRequestDto) {
        Profile profile = profileRepository.findByUser_UserId(profileRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(MsgCode.USER_NOT_FOUND));

        if (!(profileRequestDto.getNickname() == null))
            profile.setNickname(profileRequestDto.getNickname());
        if (!(profileRequestDto.getDescribeSelf() == null))
            profile.setDescribeSelf(profileRequestDto.getDescribeSelf());
        if (!(profileRequestDto.getGender() == null))
            profile.setGender(profileRequestDto.getGender());

        return Optional.of(ProfileResponseDto.toDTO(profile));
    }

    //  프로필 삭제
    @Transactional
    public void deleteProfile(Integer profileId){
        if (!profileRepository.existsById(profileId)) {
            throw new CustomException(MsgCode.PROFILE_NOT_FOUND);
        }
        else {
            profileRepository.deleteById(profileId);
        }
    }
}
