package com.example.delivery.domain.review.dto;


import com.example.delivery.domain.review.entity.Review;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSaveResponseDto {

    private final Long reviewId;

    private final Long storeId;

    private final Long userId;

    private final String content;

    private final Integer rating;

    private final LocalDateTime updatedAt;

    public static ReviewSaveResponseDto toDto(Review review){
        return new ReviewSaveResponseDto(
            review.getId(),
            review.getStore().getId(),
            review.getUser().getId(),
            review.getContent(),
            review.getRating(),
            review.getUpdatedAt()
        );
    }
}
