package com.example.delivery.review.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.dto.ReviewSaveResponseDto;
import com.example.delivery.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 저장 컨트롤러
     *
     * @param requestDto "storeId","userId","content","rating" content 만 null 가능
     * @param files      null 이여도 허용
     * @return 커스텀 응답 성공 201
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<ReviewSaveResponseDto>> saveReview(
        @RequestPart("request") @Valid ReviewSaveRequestDto requestDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        reviewService.save(requestDto, files);

        return ResponseEntity
            .status(SuccessCode.REVIEW_CREATED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED));
    }

    /**
     * 내 리뷰 조회, 가게 별 리뷰 조회, 별점 기준 리뷰 조회 메소드
     *
     * @param storeId 가게 별 리뷰 조회
     * @param userId 내 리뷰 조회
     * @param rating 별점 기준 리뷰 조회 메소드
     * @return 조회 결과와 응답 코드
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ReviewFindResponseDto>>> find(
        @RequestParam(required = false) @Min(1) Long storeId, // 가게 아이디
        @RequestParam(required = false) @Min(1) Long userId, // 유저 아이디
        @RequestParam(required = false) @Min(1) @Max(5) Long rating // 별점
    ) {

        List<ReviewFindResponseDto> responseDto = reviewService.find(storeId,userId,rating);
        return ResponseEntity.status(SuccessCode.OK.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.OK, responseDto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto<?>> deleteReviewById(
        @PathVariable("reviewId") @Min(1) Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(SuccessCode.REVIEW_DELETED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_DELETED));
    }

}
