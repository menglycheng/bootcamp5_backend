package com.checkme.CheckMe.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class AmazonClientService {
    private AmazonS3 s3client;

    private final static Logger LOGGER = LoggerFactory.getLogger(AmazonClientService.class);

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    public String upload(MultipartFile multipartFile, String folderPath) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl  + "/" + folderPath + fileName;
            uploadFileTos3bucket(fileName, file, folderPath);
            var deleteFile = file.delete();
            if (!deleteFile) {
                LOGGER.warn("File {} not deleted", fileName);
            } else {
                LOGGER.info("File {} deleted", fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    //
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    //generate unique name to the upload file
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    //upload files to s3
    private void uploadFileTos3bucket(String fileName, File file, String folderPath) {
        try {
            var putObjectRequest = new PutObjectRequest(bucketName, folderPath + fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3client.putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
