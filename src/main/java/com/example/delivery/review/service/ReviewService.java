package com.example.delivery.review.service;


import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.dto.ReviewSaveResponseDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto, List<MultipartFile> files);
    

    public void deleteReview(Long reviewId);

    public void updateReview(String content, String imgUrl, Integer rating);

    List<ReviewFindResponseDto> findByStoreId(Long storeId);

    List<ReviewFindResponseDto> find(Long storeId,Long userId,Long rating);
}
