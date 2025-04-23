package com.example.delivery.review.service;


import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.dto.ReviewSaveResponseDto;
import com.example.delivery.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewService {

    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto);
    

    public void deleteReview(Long reviewId);

    public void updateReview(String content, String imgUrl, Integer rating);

    List<ReviewFindResponseDto> findByStoreId(Long storeId);
}
