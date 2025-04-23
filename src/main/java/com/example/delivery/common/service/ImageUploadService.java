package com.example.delivery.common.service;


import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.enums.ErrorCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> uploadFile(List<MultipartFile> fileList) {

        List<String> urlList = new ArrayList<>();

        for (MultipartFile file : fileList) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)  // 업로드할 대상 버킷 이름
                .key(fileName)  // 버킷 내 저장할 경로 (위에서 만든 고유 파일명)
                .contentType(file.getContentType()) // 타입 image/png
                .build();
            try {
                s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            } catch (IOException e) {
                throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            urlList.add("https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName);
        }
        return urlList;
    }

}
