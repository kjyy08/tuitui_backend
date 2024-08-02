package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.domain.Image;
import suftware.tuitui.dto.request.ImageRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.service.ImageService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class ImageController {
    private final ImageService imageService;

    //  이미지 업로드
    //  @PostMapping("image/upload")
    //  public Optional<ImageResponseDto> uploadImage(@RequestPart(name = "request") ImageRequestDto imageRequestDto,
    //                                                @RequestPart(name = "file") MultipartFile file) throws IOException {
    //      return imageService.uploadImage(imageRequestDto, file);
    //  }

    //  전체 이미지 조회
    @GetMapping("image/list")
    public List<ImageResponseDto> getImageList(){
        return imageService.getAllImage();
    }

    //  이미지 id 기준 조회
    @GetMapping("image/{id}")
    public ResponseEntity<ImageResponseDto> getImage(@PathVariable("id") int id) {
        Optional<Image> image = imageService.getImage(id);
        if (image.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ImageResponseDto.toDto(image.get()), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}