package com.example.delivery.review.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.dto.ReviewSaveResponseDto;
import com.example.delivery.review.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)   // Spring이 어떤 Content-Type을 지원해야 하는지 명시.
    public ResponseEntity<ApiResponseDto<ReviewSaveResponseDto>> saveReview(
        @RequestPart("request") String request,
        @RequestPart(value = "file", required = false) List<MultipartFile> file
    ) throws JsonProcessingException {

        ReviewSaveRequestDto requestDto = new ObjectMapper().readValue(request, ReviewSaveRequestDto.class);

        reviewService.save(requestDto, file);

        return ResponseEntity
            .status(SuccessCode.REVIEW_CREATED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponseDto<ReviewFindResponseDto>> findByStoreId(
        @PathVariable Long storeId) {
        List<ReviewFindResponseDto> requestDto = reviewService.findByStoreId(storeId);
        return ResponseEntity.status(SuccessCode.OK.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.OK));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto<?>> deleteReviewById(@PathVariable @Min(1) Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(SuccessCode.REVIEW_DELETED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_DELETED));
    }

}
