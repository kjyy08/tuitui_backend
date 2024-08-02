package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.request.ProfileRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
        Optional<Profile> profile = profileRepository.findById(id);

        if (profile.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(ProfileResponseDto.toDTO(profile.get()));
        }
    }

    public Optional<ProfileResponseDto> getProfileByNickname(String nickname){
        Optional<Profile> profile = profileRepository.findByNickname(nickname);

        if (profile.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(ProfileResponseDto.toDTO(profile.get()));
        }
    }

    public Optional<ProfileResponseDto> getProfileByUserId(Integer id){
        Optional<Profile> profile = profileRepository.findByUser_UserId(id);

        if (profile.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(ProfileResponseDto.toDTO(profile.get()));
        }
    }

    public List<ProfileResponseDto> getProfileList() {
        List<Profile> profileList = profileRepository.findAll();
        List<ProfileResponseDto> profileResponseDtoList = new ArrayList<>();

        for (Profile profile : profileList){
            profileResponseDtoList.add(ProfileResponseDto.toDTO(profile));
        }

        return profileResponseDtoList;
    }

    public Optional<ProfileResponseDto> saveProfile(ProfileRequestDto profileRequestDto, MultipartFile file) {
        //  유저가 존재하는지 확인
        Optional<User> user = userRepository.findById(profileRequestDto.getUserId());

        if (user.isEmpty()){
            return Optional.empty();
        }
        else {
            String filePath = hostUrl + directoryPath + file.getOriginalFilename();
            Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user.get(), filePath));
            return Optional.of(ProfileResponseDto.toDTO(profile));
        }
    }

    public Optional<ProfileResponseDto> saveProfile(ProfileRequestDto profileRequestDto) {
        Optional<User> user = userRepository.findById(profileRequestDto.getUserId());

        if (user.isEmpty()){
            return Optional.empty();
        }
        else {
            String filePath = hostUrl + directoryPath + basicProfileImgPath;
            Profile profile = profileRepository.save(ProfileRequestDto.toEntity(profileRequestDto, user.get(), filePath));
            return Optional.of(ProfileResponseDto.toDTO(profile));
        }
    }

    @Transactional
    public Optional<ProfileResponseDto> updateProfile(ProfileRequestDto profileRequestDto){
        Optional<Profile> profile = profileRepository.findByUser_UserId(profileRequestDto.getUserId());

        if (profile.isEmpty()){
            return Optional.empty();
        }

        else {
            if (!(profileRequestDto.getNickname() == null))
                profile.get().setNickname(profileRequestDto.getNickname());
            if (!(profileRequestDto.getDescribeSelf() == null))
                profile.get().setDescribeSelf(profileRequestDto.getDescribeSelf());
            if (!(profileRequestDto.getGender() == null))
                profile.get().setGender(profileRequestDto.getGender());
            //  if (!(profileRequestDto.getBirth() == null))
            //      profile.get().setBirth(profileRequestDto.getBirth());

            return Optional.of(ProfileResponseDto.toDTO(profile.get()));
        }
    }

    //  프로필 삭제
    @Transactional
    public Message deleteProfile(Integer profileId){
        Message message = new Message();

        if (profileRepository.existsById(profileId)) {
            profileRepository.deleteById(profileId);
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.DELETE_SUCCESS.getMsg());
            return message;
        }
        else {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("profileId: " + profileId + ", " + MsgCode.PROFILE_NOT_FOUND.getMsg());
            return message;
        }
    }
}
