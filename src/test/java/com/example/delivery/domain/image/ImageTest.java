package com.example.delivery.domain.image;

import com.example.delivery.domain.image.dto.ImageResponseDto;
import com.example.delivery.domain.image.entity.ImageType;
import com.example.delivery.domain.image.service.ImageService;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.User.Role;
import com.example.delivery.domain.user.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest
@Transactional
public class ImageTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ImageService imageService;


    @Test
    void 사진_저장() throws IOException {
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

        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder().content("너무 맛있어요!")
            .rating(5).storeId(store.getId()).build();

        // when
        Review review = reviewRepository.save(
            Review.of(store, user, requestDto.getContent(), requestDto.getRating())
        );


        // 진짜 사진

        byte[] fileContent1 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ee.png"));
        byte[] fileContent2 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ww.png"));

        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("files", "ee.png", "image/png", fileContent1);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("files", "ww.png", "image/png", fileContent2);
        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);

//
//        // Mock MultipartFile 생성 (이미지 흉내)
//        MockMultipartFile mockFile = new MockMultipartFile(
//            "file",                         // 파라미터 이름
//            "chicken.jpg",                  // 파일 이름
//            "image/jpeg",                   // Content-Type
//            "image-content".getBytes()      // 파일 내용
//        );
//        List<MultipartFile> files = List.of(mockFile);
        imageService.fileSave(files, review.getId(), ImageType.REVIEW);

        List<ImageResponseDto> list = imageService.find(review.getId(),ImageType.REVIEW);
        assert !list.isEmpty();
    }

    @Test
    void 사진_저장하고_제발_삭제() throws IOException {
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

        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder().content("너무 맛있어요!")
            .rating(5).storeId(store.getId()).build();

        // when
        Review review = reviewRepository.save(
            Review.of(store, user, requestDto.getContent(), requestDto.getRating())
        );


        // 진짜 사진

        byte[] fileContent1 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ee.png"));
        byte[] fileContent2 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ww.png"));

        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("files", "ee.png", "image/png", fileContent1);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("files", "ww.png", "image/png", fileContent2);
        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);

//
//        // Mock MultipartFile 생성 (이미지 흉내)
//        MockMultipartFile mockFile = new MockMultipartFile(
//            "file",                         // 파라미터 이름
//            "chicken.jpg",                  // 파일 이름
//            "image/jpeg",                   // Content-Type
//            "image-content".getBytes()      // 파일 내용
//        );
//        List<MultipartFile> files = List.of(mockFile);
        imageService.fileSave(files, review.getId(), ImageType.REVIEW);
        // then
        Review savedReview = reviewRepository.findById(review.getId())
            .orElseThrow(() -> new RuntimeException("리뷰 저장 실패"));


        //when
        List<ImageResponseDto> image = imageService.find(savedReview.getId(), ImageType.REVIEW);
        imageService.delete(image.stream().map(ImageResponseDto::getImgKey).toList(), ImageType.REVIEW,
            savedReview.getId());

    }


    @Test
    void 사진_수정() throws IOException {
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

        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder().content("너무 맛있어요!")
            .rating(5).storeId(store.getId()).build();

        // when
        Review review = reviewRepository.save(
            Review.of(store, user, requestDto.getContent(), requestDto.getRating())
        );


        // 진짜 사진

        byte[] fileContent1 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ee.png"));
        byte[] fileContent2 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ww.png"));

        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("files", "ee.png", "image/png", fileContent1);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("files", "ww.png", "image/png", fileContent2);
        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);

//
//        // Mock MultipartFile 생성 (이미지 흉내)
//        MockMultipartFile mockFile = new MockMultipartFile(
//            "file",                         // 파라미터 이름
//            "chicken.jpg",                  // 파일 이름
//            "image/jpeg",                   // Content-Type
//            "image-content".getBytes()      // 파일 내용
//        );
//        List<MultipartFile> files = List.of(mockFile);
        imageService.fileSave(files, review.getId(), ImageType.REVIEW);

        //수정 파일
        byte[] fileContent3 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/join.png"));
        byte[] fileContent31 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/변수의종류.png"));
        byte[] fileContent32 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/4444.png"));

        MockMultipartFile mockMultipartFile3 = new MockMultipartFile("files", "join.png", "image/png", fileContent3);
        MockMultipartFile mockMultipartFile31 = new MockMultipartFile("files", "변수의종류.png", "image/png", fileContent31);
        MockMultipartFile mockMultipartFile32 = new MockMultipartFile("files", "4444.png", "image/png",
            fileContent32);
        List<MultipartFile> files5 = new ArrayList<>();
        files5.add(mockMultipartFile3);
        files5.add(mockMultipartFile31);
        files5.add(mockMultipartFile32);



        //when


        imageService.update(review.getId(),ImageType.REVIEW,files5);

        List<ImageResponseDto> list = imageService.find(review.getId(),ImageType.REVIEW);
        System.out.println(list);
        //
        assert !list.isEmpty();
;    }
}
