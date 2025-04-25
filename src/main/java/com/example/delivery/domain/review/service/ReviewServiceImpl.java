package com.example.delivery.domain.review.service;

import com.example.delivery.common.service.ImageUploadService;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import com.example.delivery.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    private final ImageUploadService imageUploadService;

//    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ReviewSaveResponseDto save(ReviewSaveRequestDto requestDto, List<MultipartFile> files) {

        if(files != null && !files.isEmpty()){
            List<String> urlList = imageUploadService.uploadFile(files);
            urlList.forEach(System.out::println);
        }


//        Store store = storeRepository.findById(requestDto.getStoreId());
//        User user = userRepository.findById(requestDto.getUserId());
//
//        Review review = new Review(
//            requestDto.getContent(),
//            requestDto.getImgUrl(),
//            requestDto.getRating(),
//            store,
//            user
//            );
//
//        return new ReviewSaveResponseDto(reviewRepository.save(review));
        return null;
    }

    @Override
    public List<ReviewFindResponseDto> findByStoreId(Long storeId) {

//        StoreRepository.findAllByStoreId(storeId);
//        reviewRepository.findAllByStoreId(storeId);

        return null;
    }

    /**
     * 리뷰 삭제 메소드
     * @param reviewId 리뷰 ID
     */
    @Transactional
    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.delete(reviewRepository.findByIdOrElseThrow(reviewId));
    }

    @Override
    public void updateReview(String content, String imgUrl, Integer rating) {

    }
}
