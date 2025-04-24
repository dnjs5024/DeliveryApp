package com.example.delivery.domain.store.dto;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;

import java.time.LocalTime;

public record StoreResponseDto (
        Long id,
        String name,
        LocalTime openTime,
        LocalTime closeTime,
        int minOrderPrice,
        StoreStatus status
){ //정적 팩토리 메서드(생성 메서드) Store 엔티티를 받아서 Dto로 변환해주는 메서드, 서비스나 컨트롤러에서 사용하기 위해서
    public static StoreResponseDto from(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                store.getStatus()
        );
    }
}
