package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.StoreRequestDto;
import com.example.delivery.domain.store.dto.StoreResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    /**
     * 가게를 생성합니다.
     *
     * @param storeRequestDto 생성할 가게 정보
     * @param ownerId 요청한 유저(사장님) ID
     * @return 생성된 가게 응답 DTO
     */
    StoreResponseDto createStore(StoreRequestDto storeRequestDto, Long ownerId);

    /**
     * 가게 정보를 수정합니다.
     *
     * @param storeId 수정할 가게 ID
     * @param ownerId 요청한 유저(사장님) ID
     * @param storeRequestDto 수정할 가게 정보
     * @return 수정된 가게 응답 DTO
     */
    StoreResponseDto updateStore(Long storeId, Long ownerId, StoreRequestDto storeRequestDto);

    /**
     * 단일 가게 정보를 조회합니다. (메뉴 포함, OPEN 상태만 조회)
     *
     * @param storeId 가게 ID
     * @return 가게 응답 DTO
     */
    StoreResponseDto getStore(Long storeId); // 가게 단건 조회
    /**
     * 사장님이 운영하는 모든 가게 목록 조회
     *
     * @param ownerId 사장님 유저 ID
     * @return 가게 응답 DTO 리스트
     */
    List<StoreResponseDto> getMyStores(Long ownerId);
    Page<StoreResponseDto> getStoresByPage(Pageable pageable, String keyword);
    List<StoreResponseDto> getStoresByCursor(Long lastId, int size, String keyword);
    void changeStoreStatus(Long storeId, Long userId, StoreStatus status);


}
