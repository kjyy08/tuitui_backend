//package suftware.tuitui.controller;
//
//import suftware.tuitui.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/files")    // 버킷 관련 기본 경로
//public class S3Controller {
//    private final S3Service s3Service;
//
//    @PostMapping("/upload/{path}")
//    public ResponseEntity<String> uploadFile(@PathVariable("path") String path, @RequestParam MultipartFile file) {
//        String folder;
//        if (path.equals("image")){
//            folder = "image_image";
//        }
//        else if (path.equals("profile")){
//            folder = "profile_image";
//        }
//        else{
//            return ResponseEntity.badRequest().body("Invalid upload type");
//        }
//        String url = s3Service.uploadFile(file, folder);
//        return ResponseEntity.ok(url);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
//    }
//}