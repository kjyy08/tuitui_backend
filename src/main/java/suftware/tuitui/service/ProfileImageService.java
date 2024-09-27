package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.ProfileImage;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.repository.ProfileImageRepository;
import suftware.tuitui.repository.ProfileRepository;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileImageService {
    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

    private final static String basicProfileImageName = "basic_profilie_image.png";

    @Value("${cloud.aws.cloud-front}")
    private String cloudFrontUrl;

    public Optional<ImageResponseDto> readProfileImage(Integer profileId){
        ProfileImage profileImage = profileImageRepository.findByProfileId(profileId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        return Optional.of(ImageResponseDto.toDto(profileImage));
    }

    //  프로필 사진 생성 및 업로드
    public Optional<ImageResponseDto> uploadProfileImage(Integer profileId, String path, MultipartFile file){
        try {
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
            String fileUrl = s3Service.upload(path, file);
            ProfileImage profileImage = profileImageRepository.save(ProfileImage.builder()
                    .profile(profile)
                    .imgUrl(fileUrl)
                    .build());

            return Optional.of(ImageResponseDto.toDto(profileImage));
        } catch (IOException e){
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL);
        }
    }

    //  프로필 사진 업데이트
    @Transactional
    public Optional<ImageResponseDto> updateProfileImage(Integer profileId, String path, MultipartFile file){
        try {
            Optional<ProfileImage> profileImage = profileImageRepository.findByProfileId(profileId);

            //  설정된 프로필 이미지가 없는 경우
            if (profileImage.isEmpty()){
                Profile profile = profileRepository.findById(profileId)
                        .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
                String fileUrl = s3Service.upload(path, file);
                ProfileImage profileImgEntity = profileImageRepository.save(ProfileImage.builder()
                        .profile(profile)
                        .imgUrl(fileUrl)
                        .build());

                return Optional.of(ImageResponseDto.toDto(profileImgEntity));
            }

            String currentFileUrl = profileImage.get().getImgUrl();
            String currentFileName = currentFileUrl.substring(currentFileUrl.lastIndexOf('/') + 1);

            s3Service.delete(path, currentFileName);

            String fileUrl = s3Service.upload(path, file);
            profileImage.get().setImgUrl(fileUrl);

            return Optional.of(ImageResponseDto.toDto(profileImage.get()));
        } catch (IOException e){
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL);
        }
    }

    @Transactional
    public Optional<ImageResponseDto> deleteProfileImage(Integer profileId, String path){
        ProfileImage profileImage = profileImageRepository.findByProfileId(profileId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        //  기본 프로필 사진일 경우에는 사진을 지우지 않고 값을 반환
        if (profileImage.getImgUrl().equals(getBasicProfileUrl())){
            return Optional.of(ImageResponseDto.toDto(profileImage));
        }

        String currentFileUrl = profileImage.getImgUrl();
        String currentFileName = currentFileUrl.substring(currentFileUrl.lastIndexOf('/') + 1);
        //  기본 프로필 이미지 url로 변경
        profileImage.setImgUrl(getBasicProfileUrl());

        //  s3에 업로드된 프로필 이미지 삭제
        s3Service.delete(path, currentFileName);
        return Optional.of(ImageResponseDto.toDto(profileImage));
    }

    //  유저를 삭제하는 경우에 사용
    @Transactional
    public Optional<ImageResponseDto> deleteProfileImageS3(Integer userId){
        ProfileImage profileImage = profileImageRepository.findByUserId(userId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND));

        //  기본 프로필 사진일 경우에는 사진을 지우지 않고 값을 반환
        if (profileImage.getImgUrl().equals(getBasicProfileUrl())){
            return Optional.of(ImageResponseDto.toDto(profileImage));
        }

        String currentFileUrl = profileImage.getImgUrl();
        String currentFileName = currentFileUrl.substring(currentFileUrl.lastIndexOf('/') + 1);
        //  기본 프로필 이미지 url로 변경
        profileImage.setImgUrl(getBasicProfileUrl());

        //  s3에 업로드된 프로필 이미지 삭제
        s3Service.delete(S3ImagePath.PROFILE.getPath(), currentFileName);
        return Optional.of(ImageResponseDto.toDto(profileImage));
    }

    //  기본 프로필 이미지 생성
    public Optional<ImageResponseDto> createProfileBasicImage(Integer profileId){
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
        ProfileImage profileImage = profileImageRepository.save(ProfileImage.builder()
                .profile(profile)
                .imgUrl(getBasicProfileUrl())
                .build());

        return Optional.of(ImageResponseDto.toDto(profileImage));
    }

    public String getBasicProfileUrl(){
        return cloudFrontUrl + '/' +  S3ImagePath.PROFILE.getPath() + basicProfileImageName;
    }
}
