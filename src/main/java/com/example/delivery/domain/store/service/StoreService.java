package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.StoreRequestDto;
import com.example.delivery.domain.store.dto.StoreResponseDto;
import com.example.delivery.domain.store.entity.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    StoreResponseDto createStore(StoreRequestDto storeRequestDto, Long userId);
    StoreResponseDto updateStore(Long storeId, Long userId, StoreRequestDto storeRequestDto);
    StoreResponseDto getStore(Long storeId); // 가게 단건 조회
    List<StoreResponseDto> getStoresByName(String keyword);
    Page<StoreResponseDto> getStoresByPage(Pageable pageable);
    List<StoreResponseDto> getStoresByCursor(Long lastId, int size);
    void changeStoreStatus(Long storeId, Long userId, StoreStatus status);
}
