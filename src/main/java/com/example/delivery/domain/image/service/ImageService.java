package com.example.delivery.domain.image.service;

import com.example.delivery.domain.image.dto.ImageResponseDto;
import com.example.delivery.domain.image.entity.ImageType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    List<Map<String, Object>> uploadFile(List<MultipartFile> fileList) throws IOException;

    void fileSave(List<MultipartFile> files, Long targetId, ImageType type);

    List<ImageResponseDto> find(Long targetId, ImageType type);

    void update(Long targetId, ImageType imageType, List<MultipartFile> fileList);

    void delete(List<String> keys,ImageType imageType, Long targetId);
}
