package com.example.delivery.domain.review.dto;

import com.example.delivery.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewFindResponseDto {

    private final Long reviewId;

    private final Long storeId;

    private final Long userId;

    private final String content;

    private final Integer rating;

    private final String imgUrl;

    private final LocalDateTime updatedAt;

    public static ReviewFindResponseDto toDto(Review review){
        return new ReviewFindResponseDto(
            review.getId(),
            review.getStore().getId(),
            review.getUser().getId(),
            review.getContent(),
            review.getRating(),
            null,
            review.getUpdatedAt()
        );
    }
}
