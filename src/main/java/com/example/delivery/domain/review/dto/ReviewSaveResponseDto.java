package com.example.delivery.domain.review.dto;


import com.example.delivery.domain.review.entity.Review;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ReviewSaveResponseDto {

    private Long reviewId;

    private Long storeId;

    private Long userId;

    private String content;

    private Integer rating;

    private String imgUrl;

    private LocalDateTime updatedAt;

    public ReviewSaveResponseDto(Long id, String content, Integer rating, String imgUrl,LocalDateTime updatedAt) {
        this.reviewId = id;
        this.content = content;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.updatedAt = updatedAt;

    }

    public static ReviewSaveResponseDto toDto(Review review){
        return new ReviewSaveResponseDto(
            review.getId(),
            review.getContent(),
            review.getRating(),
            null,
            review.getUpdatedAt()
        );
    }
}
