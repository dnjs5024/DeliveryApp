package com.example.delivery.domain.store.dto;

import com.example.delivery.domain.menu.dto.ResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalTime;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public record StoreFindResponseDto(
    Long id,
    String name,
    LocalTime openTime,
    LocalTime closeTime,
    int minOrderPrice,
    StoreStatus status,
    List<ResponseDto> menu
) { //정적 팩토리 메서드(생성 메서드) Store 엔티티를 받아서 Dto로 변환해주는 메서드, 서비스나 컨트롤러에서 사용하기 위해서

    public static StoreFindResponseDto from(Store store) {
        return new StoreFindResponseDto(
            store.getId(),
            store.getName(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getMinOrderPrice(),
            store.getStatus(),
            store.getMenus()
                .stream()
                .map(ResponseDto::from)
                .toList()
        );
    }
}
