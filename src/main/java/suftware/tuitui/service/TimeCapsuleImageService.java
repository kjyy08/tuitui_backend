package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleImage;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.repository.TimeCapsuleImageRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class TimeCapsuleImageService {
    private final TimeCapsuleRepository timeCapsuleRepository;
    private final TimeCapsuleImageRepository timeCapsuleImageRepository;
    private final S3Service s3Service;

    public Optional<ImageResponseDto> uploadCapsuleImage(Integer id, String path, MultipartFile file){
        try {
            TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));
            String fileUrl = s3Service.upload(path, file);
            TimeCapsuleImage timeCapsuleImage = timeCapsuleImageRepository.save(TimeCapsuleImage.builder()
                    .timeCapsule(timeCapsule)
                    .imgUrl(fileUrl)
                    .build());

            return Optional.of(ImageResponseDto.toDto(timeCapsuleImage));
        } catch (IOException e){
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL);
        }
    }

    @Transactional
    public void deleteCapsuleImage(Integer id){
        List<TimeCapsuleImage> timeCapsuleImages = timeCapsuleImageRepository.findByCapsuleId(id);

        if (!timeCapsuleImages.isEmpty()){
            for (TimeCapsuleImage timeCapsuleImage : timeCapsuleImages){
                String fileName = timeCapsuleImage.getImgUrl().substring(timeCapsuleImage.getImgUrl().lastIndexOf('/') + 1);
                s3Service.delete(S3ImagePath.CAPSULE.getPath(), fileName);
                timeCapsuleImageRepository.delete(timeCapsuleImage);
            }
        }
    }

    @Transactional
    public void deleteCapsuleImageS3(Integer id){
        List<TimeCapsuleImage> timeCapsuleImages = timeCapsuleImageRepository.findByUserId(id);

        if (!timeCapsuleImages.isEmpty()){
            for (TimeCapsuleImage timeCapsuleImage : timeCapsuleImages){
                String fileName = timeCapsuleImage.getImgUrl().substring(timeCapsuleImage.getImgUrl().lastIndexOf('/') + 1);
                s3Service.delete(S3ImagePath.CAPSULE.getPath(), fileName);
                timeCapsuleImageRepository.delete(timeCapsuleImage);
            }
        }
    }
}
