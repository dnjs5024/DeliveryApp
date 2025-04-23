package com.example.delivery.common.service;


import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)  // 업로드할 대상 버킷 이름
            .key(fileName)  // 버킷 내 저장할 경로 (위에서 만든 고유 파일명)
            .contentType(file.getContentType()) // 타입 image/png
            .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

}
