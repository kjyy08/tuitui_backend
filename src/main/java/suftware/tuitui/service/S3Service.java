package suftware.tuitui.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFile(List<MultipartFile> files, String folder) {

        List<String> urls = new ArrayList<>();  // 반환된 이미지 url 저장
        for (MultipartFile file : files){
            String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            try{
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

                urls.add(amazonS3.getUrl(bucketName, fileName).toString());
            }
            catch(IOException e){
                e.printStackTrace();
                throw new RuntimeException("Error while uploading file to S3", e);
            }
        }
        return urls;
    }
}

//        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//        try {
//            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error while uploading file to S3", e);
//        }
//
//        return amazonS3.getUrl(bucketName, fileName).toString();