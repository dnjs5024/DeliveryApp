package com.example.delivery.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@RequiredArgsConstructor
public class ReviewSaveRequestDto {

    @Min(1)
    @NotNull
    private final Long storeId;

    @Min(1)
    @NotNull
    private final Long userId;

    @NotBlank
    @Size(max = 50)
    private final String content;

    @Min(1)
    @Max(5)
    @NotNull
    private final Integer rating;

}
