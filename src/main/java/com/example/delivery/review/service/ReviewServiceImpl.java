package com.example.delivery.review.service;

import com.example.delivery.review.dto.ReviewFindResponseDto;
import com.example.delivery.review.dto.ReviewSaveRequestDto;
import com.example.delivery.review.dto.ReviewSaveResponseDto;
import com.example.delivery.review.entity.Review;
import com.example.delivery.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

//    private final StoreRepository storeRepository;

//      private final UserRepository userRepository;

    @Transactional
    @Override
    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto) {

//        Store store = storeRepository.findById(requestDto.getStoreId());
//        User user = userRepository.findById(requestDto.getUserId());

//        Review review = new Review(
//            requestDto.getContent(),
//            requestDto.getImgUrl(),
//            requestDto.getRating(),
//            store,
//            user
//            );

//        return new ReviewSaveResponseDto(reviewRepository.save(review));
        return null;
    }

    @Override
    public List<ReviewFindResponseDto> findByStoreId(Long storeId) {

//        StoreRepository.findAllByStoreId(storeId);
//        reviewRepository.findAllByStoreId(storeId);

        return null;
    }

    @Override
    public void deleteReview(Long reviewId) {

    }

    @Override
    public void updateReview(String content, String imgUrl, Integer rating) {

    }
}
