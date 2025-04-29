package com.example.delivery.domain.review;

import com.example.delivery.common.exception.BadRequestException;
import com.example.delivery.domain.image.service.ImageServiceImpl;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderMenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.review.dto.ReviewFindResponseDto;
import com.example.delivery.domain.review.dto.ReviewSaveRequestDto;
import com.example.delivery.domain.review.dto.ReviewSaveResponseDto;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class ReviewTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ImageServiceImpl imageUploadService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderMenuRepository orderMenuRepository;
    @Autowired
    ReviewService reviewService;


    @Transactional
    @Test
    void 리뷰_저장_실패_주문_미완료() throws IOException {
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

        Menu menu = Menu.of(store, "치킨", 18000, "바삭한 후라이드 치킨");
        menuRepository.save(menu);

        OrderMenu orderMenu = new OrderMenu(null, menu, 2);
        orderMenuRepository.save(orderMenu);

        Order order = Order.create(user, store, List.of(orderMenu), "조심히 와주세요!");
        orderRepository.save(order);

        // 주문 상태를 COMPLETE로 변경하지 않음 (즉, 실패 유발)

        orderMenu.setOrder(order); // 관계 연결

        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder()
            .content("리뷰 작성 시도")
            .rating(5)
            .storeId(store.getId())
            .build();

        byte[] fileContent = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ee.png"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "ee.png", "image/png", fileContent);
        List<MultipartFile> files = List.of(mockMultipartFile);

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            reviewService.save(requestDto, user.getId(), files);
        });

        // then
        assertEquals("완료된 주문 건에 대해서만 리뷰 작성이 가능합니다.", exception.getMessage());
    }



    @Test
    void 리뷰_업데이트() throws IOException {
        //기븐이요
        User user = userRepository.save(new User("jun@example.com", "encoded-password", Role.USER, "준이"));

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
        // Mock MultipartFile 생성 (이미지 흉내)
        // 진짜 사진

        List<MultipartFile> files = new ArrayList<>();


        // 웬이요
        reviewService.updateReview(requestDto, user.getId(), review.getId(), files);

        //업데이트 데이터 체크요 ㅁ니;ㅇ럼냐ㅗㅓ랴매ㅏㅣㅗ
        List<Review> review2 = reviewRepository.findByUserId(user.getId());


        assert review2.get(0).getContent().equals("너무 맛없어요!");
        assert review2.get(0).getRating() == 1;

    }

    @Test
    void 내가_작성한_리뷰() {
        //given

        User user =userRepository.save(new User("jun@example.com", "encoded-password", Role.USER, "준이"));

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

        List<ReviewFindResponseDto> review2 = reviewService.findUserId(user.getId());

        assert review2.get(0).getContent().equals("너무 맛있어요!");
        assert review2.get(0).getRating() == 5;

    }

    @Test
    void 필터_조회_리뷰데스() {
        //given
        User user =userRepository.save(new User("jun@example.com", "encoded-password", Role.USER, "준이"));

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
        List<Review> review2 = reviewRepository.findByUserId(user.getId());

        assert list.get(0).getContent().equals("너무 맛있어요!");
        assert list.get(0).getRating() == 5;
    }


    @Transactional
    @Test

    void 리뷰_저장_성공() throws IOException {
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

        // 3. 가짜 Menu 생성
        Menu menu1 = Menu.of(store, "치킨", 18000, "바삭한 후라이드 치킨");
        Menu menu2 = Menu.of(store, "피자", 22000, "고소한 치즈 피자");

        menuRepository.save(menu1);
        menuRepository.save(menu2);

        // 4. OrderMenu 생성 (Order는 나중에 연결)
        OrderMenu orderMenu1 = new OrderMenu(null, menu1, 2); // 치킨 2개
        OrderMenu orderMenu2 = new OrderMenu(null, menu2, 1); // 피자 1개
        List<OrderMenu> orderMenus = Arrays.asList(orderMenu1, orderMenu2);

        orderMenuRepository.save(orderMenu1);
        orderMenuRepository.save(orderMenu2);

        // 5. Order 생성
        Order order = Order.create(user, store, orderMenus, "초인종 누르지 마세요!");

        orderRepository.save(order);

        // 6. Order를 OrderMenu에 다시 연결
        orderMenus.forEach(orderMenu -> orderMenu.setOrder(order));

        // 7. 주문 상태를 COMPLETE로 변경
        order.changeStatus(OrderStatus.COMPLETE);


        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder().content("너무 맛있어요!")
            .rating(5).storeId(store.getId()).build();
        // 진짜 사진

        byte[] fileContent1 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ee.png"));
        byte[] fileContent2 = Files.readAllBytes(Path.of("C:/Users/dnjs7/OneDrive/사진/스크린샷/ww.png"));

        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("files", "ee.png", "image/png", fileContent1);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("files", "ww.png", "image/png", fileContent2);
        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);

        // when

        ReviewSaveResponseDto responseDto = reviewService.save(requestDto, user.getId(),files); // 실제 저장 서비스 실행

//
//        // Mock MultipartFile 생성 (이미지 흉내)
//        MockMultipartFile mockFile = new MockMultipartFile(
//            "file",                         // 파라미터 이름
//            "chicken.jpg",                  // 파일 이름
//            "image/jpeg",                   // Content-Type
//            "image-content".getBytes()      // 파일 내용
//        );
//        List<MultipartFile> files = List.of(mockFile);

        // then
        Review savedReview = reviewRepository.findById(responseDto.getReviewId())
            .orElseThrow(() -> new RuntimeException("리뷰 저장 실패"));

        assert savedReview.getContent().equals("너무 맛있어요!");
        assert savedReview.getRating() == 5;
        assert savedReview.getUser().getId().equals(user.getId());

    }

}
