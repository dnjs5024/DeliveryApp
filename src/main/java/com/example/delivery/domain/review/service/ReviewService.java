package com.example.delivery.domain.review.service;


import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import com.example.delivery.domain.review.dto.ReviewUpdateRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto, Long userId, List<MultipartFile> files);


    public void deleteReview(Long reviewId,Long userId);

    public void updateReview(ReviewUpdateRequestDto requestDto,Long userId,Long reviewId);

    List<ReviewFindResponseDto> findByFilter(Long storeId,Long rating);

    List<ReviewFindResponseDto> findUserId(Long userId);
}
