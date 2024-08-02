package suftware.tuitui.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while uploading file to S3", e);
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
}
