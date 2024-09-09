package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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

    @Transactional
    public Optional<ImageResponseDto> updateProfileImage(Integer id, String path, MultipartFile file){
        try {
            ProfileImage profileImage = profileImageRepository.findByProfileId(id)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

            String currentFileUrl = profileImage.getImgUrl();
            String currentFileName = currentFileUrl.substring(currentFileUrl.lastIndexOf('/') + 1, currentFileUrl.lastIndexOf('.'));

            s3Service.delete(path, currentFileName);

            String fileUrl = s3Service.upload(path, file);
            profileImage.setImgUrl(fileUrl);

            return Optional.of(ImageResponseDto.toDto(profileImage));
        } catch (IOException e){
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL);
        }
    }
}
