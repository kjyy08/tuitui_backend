package suftware.tuitui.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${S3_BUCKET_URL}")
    private String bucketUrl;

    @Value("${cloud.aws.cloud-front}")
    private String cloudFrontUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String createFilePath(String folderPath, String fileName){
        return folderPath + fileName;
    }

    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName){
        if (fileName.isEmpty()) {
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_CREATE_FAIL);
        }

        Set<String> validExtensions = Set.of(".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG");
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        if (!validExtensions.contains(fileExtension)) {
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_CREATE_FAIL);
        }

        return fileExtension;
    }

    public String upload(String folderPath, MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());
        String filePath = createFilePath(folderPath, fileName);

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata));

        return amazonS3.getUrl(folderPath.substring(0, folderPath.length() - 1), fileName).toString().replace(bucketUrl, cloudFrontUrl);
    }

    public List<String> upload(String folderPath, List<MultipartFile> files) throws IOException {
        List<String> fileUrlList = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = createFileName(file.getOriginalFilename());
            String filePath = createFilePath(folderPath, fileName);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata));
            fileUrlList.add(amazonS3.getUrl(folderPath.substring(0, folderPath.length() - 1), fileName).toString().replace(bucketUrl, cloudFrontUrl));
        }

        return fileUrlList;
    }

    public void delete(String folderPath, String fileName){
        try{
            String filePath = folderPath.concat(fileName);
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, filePath));
        } catch (RuntimeException e){
            throw new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_DELETE_FAIL);
        }
    }
}
