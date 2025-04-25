package com.example.delivery.domain.image.service;

import com.example.delivery.domain.image.entity.ImageType;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    List<String> uploadFile(List<MultipartFile> fileList);

    void fileSave(List<MultipartFile> files, Long targetId, ImageType type);

    List<String> find(Long targetId, ImageType type);

    void update();
    void delete();
}
