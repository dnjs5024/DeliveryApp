package com.example.delivery.domain.image.dto;

import com.example.delivery.domain.image.entity.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResponseDto {

    private final String imgUrl;

    private final String imgKey;

    public static ImageResponseDto toDto(Image image) {
        return new ImageResponseDto(
            image.getImgUrl(),
            image.getImgKey()
        );
    }

}
