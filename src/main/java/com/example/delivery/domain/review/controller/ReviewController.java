package com.example.delivery.domain.review.controller;

import com.example.delivery.common.annotation.ImageValid;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import com.example.delivery.domain.review.dto.ReviewUpdateRequestDto;
import com.example.delivery.domain.review.service.ReviewService;
import com.example.delivery.domain.user.dto.SessionUserDto;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
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
    @ImageValid
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<ReviewSaveResponseDto>> saveReview(
        @RequestPart("request") @Valid ReviewSaveRequestDto requestDto,
        @SessionAttribute(name = "loginUser", required = false) SessionUserDto loginUser, // 유저 아이디
        @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        ReviewSaveResponseDto responseDto = reviewService.save(requestDto, 1L, files);

        return ResponseEntity
            .status(SuccessCode.REVIEW_CREATED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED, responseDto));
    }

    /**
     * 가게 별 리뷰 조회, 별점 기준 리뷰 조회 메소드
     *
     * @param storeId 가게 별 리뷰 조회
     * @param rating  별점 기준 리뷰 조회 메소드
     * @return 조회 결과와 응답 코드
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ReviewFindResponseDto>>> findByFilter(
        @RequestParam(required = false) @Min(1) Long storeId, // 가게 아이디
        @RequestParam(required = false) @Min(1) @Max(5) Long rating // 별점
    ) {
        List<ReviewFindResponseDto> responseDto = reviewService.findByFilter(storeId, rating);

        return ResponseEntity.status(SuccessCode.OK.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.OK, responseDto));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponseDto<List<ReviewFindResponseDto>>> findByUserId(
        @SessionAttribute(name = "loginUser", required = false) SessionUserDto loginUser // 유저 아이디
    ) {
        List<ReviewFindResponseDto> responseDto = reviewService.findUserId(loginUser.getId());

        return ResponseEntity.status(SuccessCode.OK.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.OK, responseDto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto<?>> deleteById(
        @PathVariable("reviewId") @Min(1) Long reviewId,
        @SessionAttribute(name = "loginUser", required = false) SessionUserDto loginUser // 유저 아이디
    ) {
        reviewService.deleteReview(reviewId, loginUser.getId());
        return ResponseEntity.status(SuccessCode.REVIEW_DELETED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_DELETED));
    }

    @ImageValid
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto<?>> updateById(
        @PathVariable("reviewId") @Min(1) Long reviewId,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @RequestPart("request") @Valid ReviewUpdateRequestDto requestDto,
        @SessionAttribute(name = "loginUser", required = false) SessionUserDto loginUser // 유저 아이디
    ) {
        reviewService.updateReview(requestDto, loginUser.getId(), reviewId, files);
        return ResponseEntity.status(SuccessCode.REVIEW_UPDATED.getHttpStatus())
            .body(ApiResponseDto.success(SuccessCode.REVIEW_UPDATED));
    }
}
