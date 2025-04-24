package com.example.delivery.domain.order.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@Jacksonized
public class OrderRequestDTO {
    @NotNull
    private final Long storeId;
    @NotNull
    private final List<OrderMenuRequestDTO> menus;
    private final String requestMessage; //손님 요청사항
}