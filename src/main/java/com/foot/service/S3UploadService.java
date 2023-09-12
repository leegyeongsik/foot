package com.foot.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3UploadService {

    private static final String S3_BUCKET_DIRECTORY_NAME = "shoes";
    private static final String S3_BUCKET_DIRECTORY_MESSAGE_NAME = "messageimg";

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile) throws IOException  {
        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 실제 S3 bucket 디렉토리명 설정
        // 파일명 중복을 방지하기 위한 UUID 추가
        String fileName = S3_BUCKET_DIRECTORY_NAME + "/" + UUID.randomUUID() + "." + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
        }
        return URLDecoder.decode(amazonS3Client.getUrl(bucket, fileName).toString(), "utf-8");
    }

    public void deleteFile(String fileName)  {
        String splitStr = ".com/";

        String filename = fileName.substring(fileName.lastIndexOf(splitStr) + splitStr.length());
        System.out.println(filename);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
    }


    public String uploadMsgImage(File file , String username) throws IOException  {

        String fileName = S3_BUCKET_DIRECTORY_MESSAGE_NAME + "/" + UUID.randomUUID() + "." + username;
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        return URLDecoder.decode(amazonS3Client.getUrl(bucket, fileName).toString(), "utf-8");
    }

}
