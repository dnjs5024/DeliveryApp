package com.example.delivery.domain.review.repository;

import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.review.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<Image,Long> {
    default Image findByIdOrElseThrow(Long reviewImageId){
        return findById(reviewImageId).orElseThrow(
            () -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }
}
