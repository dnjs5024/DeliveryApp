package com.example.delivery.domain.store.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.store.dto.StoreFindResponseDto;
import com.example.delivery.domain.store.dto.StoreRequestDto;
import com.example.delivery.domain.store.dto.StoreResponseDto;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.dto.SessionUserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    /**
     * 가게 생성 API
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<StoreResponseDto>> createStore(@Valid
                                                        @RequestBody StoreRequestDto storeRequestDto,
                                                        @SessionAttribute("loginUser") SessionUserDto loginUser // 얘는 세션에 저장되어있는 값을 바로 꺼내서 쓰는 것
                                                        ) {
        StoreResponseDto responseDto = storeService.createStore(storeRequestDto, loginUser.getId());
        return ResponseEntity.status(SuccessCode.STORE_CREATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.STORE_CREATED, responseDto)
        );
    }
    /**
     * 내 가게 수정 API
     */
    @PutMapping("/my/{storeId}")
    public ResponseEntity<ApiResponseDto<StoreResponseDto>> updateStore(@PathVariable Long storeId,
                                                                        @Valid @RequestBody StoreRequestDto requestDto,
                                                                        @SessionAttribute("loginUser") SessionUserDto loginUser ) {
        StoreResponseDto responseDto = storeService.updateStore(storeId, loginUser.getId(), requestDto);
        return ResponseEntity.status(SuccessCode.STORE_UPDATED.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.STORE_UPDATED, responseDto));

    }
    /**
     * 가게 단건 조회 API
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<StoreFindResponseDto>> getStore(@PathVariable Long storeId) {
        StoreFindResponseDto responseDto = storeService.getStore(storeId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.STORE_READ_SUCCESS, responseDto));

    }
    /**
     * 페이지 기반 전체 가게 조회 API
     */
    @GetMapping("/pages")
    public ResponseEntity<ApiResponseDto<Page<StoreResponseDto>>> getStorePages(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<StoreResponseDto> responseDto = storeService.getStoresByPage(pageable, keyword);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.STORE_PAGING_SUCCESS, responseDto));
    }

    /**
     * 커서 기반 전체 가게 조회 API
     */
    @GetMapping("/cursor")
    public ResponseEntity<ApiResponseDto<List<StoreResponseDto>>> getStoresByCursor(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        List<StoreResponseDto> stores = storeService.getStoresByCursor(lastId, size,keyword);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.STORE_READ_SUCCESS, stores));
    }
    /**
     * 가게 상태 변경 API
     */
    @PatchMapping("/my/{storeId}/status")
    public ResponseEntity<ApiResponseDto<Void>> changeStoreStatus(
            @PathVariable Long storeId,
            @RequestParam StoreStatus status,
            @SessionAttribute("loginUser") SessionUserDto loginUser
            ) {
        storeService.changeStoreStatus(storeId, loginUser.getId(), status);
        return ResponseEntity.status(SuccessCode.STORE_UPDATED.getHttpStatus()).body(ApiResponseDto.success(SuccessCode.STORE_UPDATED));

    }

    //  내 가게 전체 보기
    @GetMapping("/my")
    public ResponseEntity<ApiResponseDto<List<StoreResponseDto>>> getMyStores(@SessionAttribute SessionUserDto loginUser) {
        List<StoreResponseDto> stores = storeService.getMyStores(loginUser.getId());
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.STORE_READ_SUCCESS, stores));
    }

}
