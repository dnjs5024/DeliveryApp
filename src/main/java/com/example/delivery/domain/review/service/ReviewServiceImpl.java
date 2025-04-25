package com.example.delivery.domain.review.service;

import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.image.entity.ImageType;
import com.example.delivery.domain.image.service.ImageService;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
import com.example.delivery.domain.review.dto.ReviewUpdateRequestDto;
import com.example.delivery.domain.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final ImageService imageUploadService;


    /**
     * @param requestDto 별점,리뷰 내용 ,가게 식별자
     * @param userId     작성한 유저 식별자
     * @param files      이미지
     * @return
     */
    @Transactional
    @Override
    public ReviewSaveResponseDto save(
        ReviewSaveRequestDto requestDto,
        Long userId,
        List<MultipartFile> files
    ) {

        Store store = storeRepository.findById(requestDto.getStoreId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewRepository.save( // 리뷰 저장
            Review.of(store, user, requestDto.getContent(), requestDto.getRating()));

        if (files != null && !files.isEmpty()) { // 이미지가 있는지 체크
            imageUploadService.fileSave(files, review.getId(), ImageType.REVIEW); // 사진이 있으면 저장
        }
        return ReviewSaveResponseDto.toDto(review);
    }


    /**
     * 여러 필터 조건으로 조회하는 메소드 1.가게 2.별점 순 필터 조건값 제외하면 나머지는 null 임
     *
     * @param storeId 가게 식별자
     * @param rating  별점
     * @return 조회 결과
     */
    @Override
    public List<ReviewFindResponseDto> findByFilter(Long storeId, Long rating) {

        List<Review> list = new ArrayList<>();

        if (storeId != null) { // 가게 기준 조회인지 체크
            storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
            list = reviewRepository.findByStoreId(storeId);
        }
        if (rating != null) { // 별점 기준 조회인지 체크

            if (reviewRepository.findByRating(rating).isEmpty()) {
                throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
            }
            list = reviewRepository.findByRating(rating);
        }

        if (list == null || list.isEmpty()) {
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return list.stream().map(ReviewFindResponseDto::toDto).toList();
    }

    /**
     * 내가 쓴 리뷰 조회하는 메소드
     *
     * @param userId 유저 id
     * @return 조회 결과
     */
    @Override
    public List<ReviewFindResponseDto> findUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        List<Review> list = reviewRepository.findByUserId(userId);
        if (list == null || list.isEmpty()) {
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return list.stream().map(ReviewFindResponseDto::toDto).toList();
    }

    /**
     * 리뷰 삭제 메소드
     *
     * @param reviewId 리뷰 ID
     * @param userId
     */
    @Transactional
    @Override
    public void deleteReview(Long reviewId, Long userId) {
        isSelf(userId);
        reviewRepository.delete(reviewRepository.findByIdOrElseThrow(reviewId));
    }


    /**
     * 본인 맞는지 체크하고 업데이트
     *
     * @param requestDto content, rating content 는 null 가능
     * @param userId     유저 맞는지 체크 위해
     * @param reviewId   수정 할 리뷰
     */
    @Transactional
    @Override
    public void updateReview(ReviewUpdateRequestDto requestDto, Long userId, Long reviewId) {
        isSelf(userId);
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);
        review.update(requestDto.getContent(), requestDto.getRating());
    }

    /**
     * 본인 맞는지 체크
     *
     * @param userId 유저 아이디
     */
    public void isSelf(Long userId) {
        if (!userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND))
            .getId()
            .equals(userId)) {
            throw new BadRequestException(ErrorCode.UNAUTHORIZED);
        }
    }

}
