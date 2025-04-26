package com.example.delivery.domain.order.dto.requestDTO;

import com.example.delivery.domain.order.entity.OrderMenu;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class OrderMenuRequestDTO{
    @NotNull
    private final Long menuId;
    @NotNull
    private final Integer quantity; //수량

    //toEntity
    public OrderMenu toEntity(Menu menu) {
        return new OrderMenu(null, menu, this.quantity);
    }
}
