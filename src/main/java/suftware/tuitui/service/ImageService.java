package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.ImageRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.repository.ImageRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${S3_BUCKET_URL}")
    private String origianlUrl;

    @Value("${cloud.aws.cloud-front}")
    private String hostUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Optional<Image> getImage(Integer id)
    {
        return imageRepository.findById(id);

    }

    public List<ImageResponseDto> getAllImage(){
        List<Image> imageList = imageRepository.findAll();
        List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

        for (Image image : imageList){
            imageResponseDtoList.add(ImageResponseDto.toDto(image));
        }

        return imageResponseDtoList;
    }

    //  public Optional<ImageResponseDto> uploadImage(String path, ){
    //
    //  }


    //  uploadImages: S3에 이미지를 저장하고 반환된 URL을 DB에 저장
    //  Parameters: {path: S3 폴더 이름}, {ImageRequestDto: TimeCapsule_id}, {file: 이미지}
    public Optional<ImageResponseDto> uploadImage(String path, Integer capsuleId, MultipartFile file) throws IOException {
        logger.info("ImageService.uploadImages ---------- Starting the image upload process. Path: {}, TimeCapsule ID: {}", path, capsuleId);
        String url;     // 반환된 URL을 저장할 변수

        // TimeCapsule 조회
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new NoSuchElementException("TimeCapsule Not Found"));
        logger.info("ImageService.uploadImages ---------- Successfully retrieved TimeCapsule ID: {}", timeCapsule.getTimeCapsuleId());

        // ImgaeName 생성 (UUID + 확장자)
        String imageName = file.getOriginalFilename();
        String fileExtension = imageName.substring(imageName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        String fileName = path + "/" + uniqueFileName;
        logger.info("ImageService.uploadImages ---------- Generated unique file with path: {}", fileName);

        // Image Uploading process
        if(!imageRepository.existsByImageName(uniqueFileName)) {    // Check Duplicated case
            try{
                logger.info("ImageService.uploadImages ---------- Uploading file to S3 bucket/{}", path);

                // Append image metadata
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

                // Returned URL after upload
                url = amazonS3.getUrl(path, uniqueFileName).toString();
                logger.info("ImageService.uploadImages ---------- File successfully uploaded to S3. {}", url);
            }
            catch (IOException e){
                logger.error("ImageService.uploadImages ---------- Failed to upload file to S3. Path: {}, Error: {}", fileName, e.getMessage());
                throw new RuntimeException("Error while uploading file to S3", e);
            }
            // Replace S3 URL to CloudFront URL
            url = url.replace(origianlUrl, hostUrl);
            Image image = imageRepository.save(ImageRequestDto.toEntity(uniqueFileName, timeCapsule, url));
            logger.info("ImageService.uploadImages ---------- Image record saved in the database. Image ID: {}, URL: {}", image.getImageId(), image.getImagePath());

            return Optional.of(ImageResponseDto.toDto(image));
        }
        else{
            Optional<Image> image = imageRepository.findByImageName(uniqueFileName);
            if (image.isPresent()){
                logger.warn("Duplicate image name detected. Returning existing image. Image ID: {}, URL: {}", image.get().getImageId(), image.get().getImagePath());
                return Optional.of(ImageResponseDto.toDto(image.get()));
            }
            else{
                logger.error("Image name exists in the repository, but the image could not be found. Image name: {}", uniqueFileName);
                throw new RuntimeException("Image name exists but the image could not be found");
            }
        }
    }

    
    // 이미지 id 기준으로 삭제
    @Transactional
    public void deleteImage(Integer imageId){
        logger.info("ImageService.deleteImage ---------- Starting the image remove process. image id: {}", imageId);
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new NoSuchElementException("Image not found with id: " + imageId));
        imageRepository.delete(image);
        logger.info("ImageService.deleteImage ---------- The target image has been deleted.");
    }
    
    // 프로필 이미지 변경


}