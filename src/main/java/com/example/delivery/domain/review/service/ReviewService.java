package com.example.delivery.domain.review.service;


import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto, List<MultipartFile> files);
    

    public void deleteReview(Long reviewId);

    public void updateReview(String content, String imgUrl, Integer rating);

    List<ReviewFindResponseDto> findByStoreId(Long storeId);
}
