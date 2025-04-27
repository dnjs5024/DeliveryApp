package com.example.delivery.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
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
