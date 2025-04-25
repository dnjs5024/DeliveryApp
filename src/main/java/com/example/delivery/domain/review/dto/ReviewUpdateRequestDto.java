package com.example.delivery.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class ReviewUpdateRequestDto {

    @Size(max = 50)
    private final String content;

    @Min(1)
    @Max(5)
    @NotNull
    private final Integer rating;

}
