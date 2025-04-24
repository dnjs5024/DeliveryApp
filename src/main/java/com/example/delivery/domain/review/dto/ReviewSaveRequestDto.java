package com.example.delivery.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSaveRequestDto {

    @Min(1)
    @NotNull
    private final Long storeId;

    @Size(max = 50)
    private final String content;

    @Min(1)
    @Max(5)
    @NotNull
    private final Integer rating;

}
