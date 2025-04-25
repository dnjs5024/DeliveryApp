package com.example.delivery.domain.image.service;


import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.image.dto.ImageResponseDto;
import com.example.delivery.domain.image.entity.Image;
import com.example.delivery.domain.image.entity.ImageType;
import com.example.delivery.domain.image.repository.ImageRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

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
    @Override
    public Map<String, Object> uploadFile(List<MultipartFile> fileList) {

        Map<String, Object> urlMap = new HashMap<>();

        for (MultipartFile file : fileList) {
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)  // 업로드할 대상 버킷 이름
                .key(key)  // 버킷 내 저장할 경로 (위에서 만든 고유 파일명)
                .contentType(file.getContentType()) // 타입 image/png
                .build();
            try {
                s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));
            } catch (IOException e) {
                throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            urlMap.put("url", "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key);
            urlMap.put("key", key);
        }
        return urlMap;
    }

    /**
     * 이미지 파일 db에 저장
     *
     * @param files    이미지 파일
     * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
     * @param type     타겟 타입 ex) 가게 , 리뷰 등
     */
    @Override
    public void fileSave(List<MultipartFile> files, Long targetId, ImageType type) {
        Map<String, Object> urlMap = uploadFile(files); // 클라우드 서버에 사진 업로드
        Iterator<String> iterator = urlMap.keySet().iterator();
        while (iterator.hasNext()) {
            String url = (String) urlMap.get(iterator.next());
            String key = (String) urlMap.get(iterator.next());
            imageRepository.save(Image.of(targetId, url, type));
        }
    }

    /**
     * 타겟 식별자,타입으로 조회한 후 결과 url 를 String List 에 담아서 반환
     *
     * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
     * @param type     타겟 타입 ex) 가게 , 리뷰 등
     * @return List<String> 이미지 url
     */
    @Override
    public List<ImageResponseDto> find(Long targetId, ImageType type) {
        return imageRepository.findByTargetIdAndTypeElseThrow(targetId, type).stream()
            .map(ImageResponseDto::toDto).toList();
    }

    @Override
    public void update(Long targetId, ImageType imageType, List<MultipartFile> fileList) {
        delete(find(targetId, imageType).stream().map(ImageResponseDto::getKey).toList());// 기존 사진 삭제
        uploadFile(fileList);
    }

    @Override
    public void delete(List<String> keys) {
        for (String key : keys) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)  // 연결 된 대상 버킷 이름
                .key(key)  // 버킷 내 삭제할 객체 키
                .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
    }
}
