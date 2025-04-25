package com.example.delivery.domain.image.dto;

import com.example.delivery.domain.image.entity.Image;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import com.example.delivery.domain.review.entity.Review;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResponseDto {

    private final String imgUrl;

    private final String key;

    public static ImageResponseDto toDto(Image image) {
        return new ImageResponseDto(
            image.getImgUrl(),
            image.getKey()
        );
    }

}
