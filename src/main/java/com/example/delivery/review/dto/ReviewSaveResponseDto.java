package com.example.delivery.review.dto;


import com.example.delivery.domain.review.entity.Review;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Getter
@RequiredArgsConstructor
public class ReviewSaveResponseDto {

    private final Long reviewId;

    private final Long storeId;

    private final Long userId;

    private final String content;

    private final Integer rating;

    private final String imgUrl;

    private final LocalDateTime updatedAt;


    @Builder
    public static ReviewSaveResponseDto toDto(Review review){
        return new ReviewSaveResponseDto(
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
