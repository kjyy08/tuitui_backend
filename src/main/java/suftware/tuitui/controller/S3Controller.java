package suftware.tuitui.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping(value = "images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file, String folderName) {
        String fileUrl = s3Service.uploadFile(file, folderName);
        return ResponseEntity.ok(fileUrl);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }
}