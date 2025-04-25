package com.example.delivery.domain.review;

import com.example.delivery.domain.image.entity.ImageType;
import com.example.delivery.domain.image.service.ImageService;
import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewUpdateRequestDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.review.service.ReviewService;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.User.Role;
import com.example.delivery.domain.user.repository.UserRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class ReviewTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ImageService imageUploadService;
    @Autowired
    ReviewService reviewService;

    @Test
    void 리뷰_업데이트() {
        //기븐이요
        User user = new User("jun@example.com", "encoded-password", Role.USER, "준이");
        userRepository.save(user);

        Store store = new Store(
            "BBQ",
            LocalTime.of(11, 0),
            LocalTime.of(23, 0),
            15000,
            StoreStatus.OPEN,
            user
        );
        storeRepository.save(store);
        // ㄼ ㅈㅈ
        Review review = reviewRepository.save(
            Review.of(store, user, "너무 맛있어요!", 1)
        );
        // 리퀘스토디티오세팅
        ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder().content("너무 맛없어요!")
            .rating(1).build();

        // 웬이요
        reviewService.updateReview(requestDto,1L,1L);

        //업데이트 데이터 체크요 ㅁ니;ㅇ럼냐ㅗㅓ랴매ㅏㅣㅗ
        List<Review> review2 = reviewRepository.findByUserId(1L);

        assert review2.get(0).getContent().equals("너무 맛없어요!");
        assert review2.get(0).getRating() == 1;

    }

    @Test
    void 내가_작성한_리뷰() {
        //given
        User user = new User("jun@example.com", "encoded-password", Role.USER, "준이");
        userRepository.save(user);

        Store store = new Store(
            "BBQ",
            LocalTime.of(11, 0),
            LocalTime.of(23, 0),
            15000,
            StoreStatus.OPEN,
            user
        );
        storeRepository.save(store);

        //리뷰 저장
        Review review = reviewRepository.save(
            Review.of(store, user, "너무 맛있어요!", 5)
        );

        List<ReviewFindResponseDto> review2 = reviewService.findUserId(1L);

        assert review2.get(0).getContent().equals("너무 맛있어요!");
        assert review2.get(0).getRating() == 5;

    }

    @Test
    void 필터_조회_리뷰데스() {
        //given
        User user = new User("jun@example.com", "encoded-password", Role.USER, "준이");
        userRepository.save(user);

        Store store = new Store(
            "BBQ",
            LocalTime.of(11, 0),
            LocalTime.of(23, 0),
            15000,
            StoreStatus.OPEN,
            user
        );

        storeRepository.save(store);

        //리뷰 저장
        Review review = reviewRepository.save(
            Review.of(store, user, "너무 맛있어요!", 5)
        );

        //when
        List<ReviewFindResponseDto> list = reviewService.findByFilter(null, 5L);

        // ㅁㅇㅁㄴㅇㄴㅁㄹㄴ아ㅣㅓ라ㅣ ㅡㅏㄴ미ㅗ아ㅣㅁ너ㅇ기모띠
        List<Review> review2 = reviewRepository.findByUserId(1L);

        assert list.get(0).getContent().equals("너무 맛있어요!");
        assert list.get(0).getRating() == 5;
    }


    @Transactional
    @Test
    void 리뷰_저장_성공() {
        // given
        User user = new User("jun@example.com", "encoded-password", Role.USER, "준이");
        userRepository.save(user);

        Store store = new Store(
            "BBQ",
            LocalTime.of(11, 0),
            LocalTime.of(23, 0),
            15000,
            StoreStatus.OPEN,
            user
        );
        storeRepository.save(store);

        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder().content("너무 맛있어요!")
            .rating(5).storeId(store.getId()).build();

        // when
        Review review = reviewRepository.save(
            Review.of(store, user, requestDto.getContent(), requestDto.getRating())
        );

        // Mock MultipartFile 생성 (이미지 흉내)
        MockMultipartFile mockFile = new MockMultipartFile(
            "file",                         // 파라미터 이름
            "chicken.jpg",                  // 파일 이름
            "image/jpeg",                   // Content-Type
            "image-content".getBytes()      // 파일 내용
        );
        List<MultipartFile> files = List.of(mockFile);
        imageUploadService.fileSave(files, review.getId(), ImageType.REVIEW);
        // then
        Review savedReview = reviewRepository.findById(review.getId())
            .orElseThrow(() -> new RuntimeException("리뷰 저장 실패"));

        assert savedReview.getContent().equals("너무 맛있어요!");
        assert savedReview.getRating() == 5;
        assert savedReview.getUser().getId().equals(user.getId());
    }

}
