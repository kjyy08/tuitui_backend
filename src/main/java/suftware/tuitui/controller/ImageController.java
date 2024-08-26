package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.domain.Image;
import suftware.tuitui.dto.request.ImageRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.service.ImageService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.util.UUID;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
@AllArgsConstructor
@RequestMapping("api/images")
public class ImageController {
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    //  이미지 업로드, 이미지 추가로 업로드 할때 사용할듯 혹은 수정?
    //  Parameters: {request: ImageRequestDto(Json 'timeCapsuleId')}, {file: 이미지}, {directory: S3 폴더 이름}
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Optional<ImageResponseDto> uploadImage(@RequestPart(name = "request") ImageRequestDto imageRequestDto,
                                                  @RequestPart(name = "file") MultipartFile file,
                                                  @RequestPart(name = "directory") String path) throws IOException {
        logger.info("========== ImageController.uploadImage: Called with parameters ==========");

        logger.info("ImageController.uploadImage ---------- Received directory path: {}", path);
        if(imageRequestDto != null){
            logger.info("ImageController.uploadImage ---------- Received ImageRequestDto: {}", imageRequestDto);
            logger.info("ImageController.uploadImage ---------- TimeCapsule ID: {}", imageRequestDto.getTimeCapsuleId());
        }
        else{
            logger.warn("ImageController.uploadImage ---------- Received ImageRequestDto is null!");
        }

        if(file != null){
            logger.info("ImageController.uploadImage ---------- Received file: {}", file.getOriginalFilename());
            logger.info("ImageController.uploadImage ---------- File size: {} bytes", file.getSize());
            logger.info("ImageController.uploadImage ---------- File content type: {}", file.getContentType());
        }
        else{
            logger.warn("ImageController.uploadImage ---------- Received file is null!");
        }

        logger.info("========== ImageController.uploadImage: Calling ImageService.uploadImages ==========");
        Optional<ImageResponseDto> returnValue = imageService.uploadImage(path, imageRequestDto.getTimeCapsuleId(), file);

        if (returnValue.isPresent()) {
            logger.info("ImageController.uploadImage ---------- Image upload successful, ImageResponseDto: {}", returnValue.get());
        } else {
            logger.warn("ImageController.uploadImage ---------- Image upload failed, no response returned.");
        }

        logger.info("========== ImageController.uploadImage: Completed ==========");

        return returnValue;
    }
    
    
    //  이미지 삭제
    //  Parameter: {id: image index}
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteImage(@PathVariable("id") int id){
        imageService.deleteImage(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.IMAGES_DELETE_SUCCESS.getMsg())
                .build());
    }

    //  전체 이미지 조회
    @GetMapping("/list")
    public List<ImageResponseDto> readImageList() {
        return imageService.getAllImage();
    }

    //  이미지 id 기준 조회
    @GetMapping("/{id}")
    public ResponseEntity<ImageResponseDto> readImage(@PathVariable("id") int id) {
        Optional<Image> image = imageService.getImage(id);
        if (image.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ImageResponseDto.toDto(image.get()), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}