package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.ImageRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.repository.ImageRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;

    @Value("${cloud.aws.cloud-front}")
    private String hostUrl;
    private String directoryPath = "image_image/";

    public Optional<Image> getImage(int id){
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

    public Optional<ImageResponseDto> uploadImage(ImageRequestDto imageRequestDto, MultipartFile file) throws IOException {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(imageRequestDto.getTimeCapsuleId())
                .orElseThrow(() -> new NoSuchElementException("TimeCapsule Not Found"));

        if (!imageRepository.existsByImageName(imageRequestDto.getImageName())) {
            String filePath = hostUrl + directoryPath + file.getOriginalFilename();
            Image image = imageRepository.save(ImageRequestDto.toEntity(imageRequestDto, timeCapsule, filePath));
            return Optional.of(ImageResponseDto.toDto(image));
        }

        else {
            Optional<Image> image = imageRepository.findByImageName(imageRequestDto.getImageName());
            return Optional.of(ImageResponseDto.toDto(image.get()));
        }
    }
}
