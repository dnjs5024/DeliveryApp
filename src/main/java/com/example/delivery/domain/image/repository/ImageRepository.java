package com.example.delivery.domain.image.repository;

import com.example.delivery.common.exception.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.image.entity.Image;
import com.example.delivery.domain.image.entity.ImageType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    default Image findByIdOrElseThrow(Long reviewImageId) {
        return findById(reviewImageId).orElseThrow(
            () -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    public List<Image> findByTargetIdAndType(Long targetId, ImageType type);

    default List<Image> findByTargetIdAndTypeElseThrow(Long targetId, ImageType type) {
        List<Image> list = findByTargetIdAndType(targetId, type);
        if (list.isEmpty()) throw new NotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        return list;
    }
}