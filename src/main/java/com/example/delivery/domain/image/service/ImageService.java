package com.example.delivery.domain.image.service;


import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.image.entity.Image;
import com.example.delivery.domain.image.entity.ImageType;
import com.example.delivery.domain.image.repository.ImageRepository;
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

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    /**
     * 외부 클라우드서버에 사진 저장 후 사진 url 리턴
     *
     * @param fileList
     * @return
     */
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

    public void fileSave(List<MultipartFile> files, Long targetId) {

        List<String> urlList = uploadFile(files); // 클라우드 서버에 사진 업로드
        for (String url : urlList) { // urlList 없으면 동작 x
            imageRepository.save(Image.of(targetId, url, ImageType.REVIEW));
        }
    }


}
