package com.example.delivery.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {

    @Min(1)
    private Long storeId;

    @Min(1)
    private Long userId;

    @NotBlank
    @Size(max = 50)
    private String content;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String imgUrl;

}
