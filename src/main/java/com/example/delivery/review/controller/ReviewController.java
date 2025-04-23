package com.example.delivery.review.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.entity.Review;
import com.example.delivery.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final  ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> saveReview(ReviewSaveRequestDto requestDto){
        reviewService.save(requestDto);
        return ResponseEntity.status(SuccessCode.REVIEW_CREATED.getHttpStatus()).body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponseDto<ReviewFindResponseDto>> findByStoreId(@PathVariable Long storeId){
        List<ReviewFindResponseDto> requestDto = reviewService.findByStoreId(storeId);
        return ResponseEntity.status(SuccessCode.REVIEW_CREATED.getHttpStatus()).body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED));
    }

}
