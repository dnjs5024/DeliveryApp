package com.example.delivery.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewSaveRequestDto {

    @Min(1)
    @NotNull
    private Long storeId;

    @Min(1)
    @NotNull
    private Long userId;

    @NotBlank
    @Size(max = 50)
    private String content;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;

}
