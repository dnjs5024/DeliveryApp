package com.example.delivery.domain.order.dto.responseDTO;

import com.example.delivery.domain.order.entity.OrderMenu;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderMenuResponseDTO {
    private final Long menuId;
    private final String menuName;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public static OrderMenuResponseDTO toDTO(OrderMenu orderMenu) {
        return OrderMenuResponseDTO.builder()
                .menuId(orderMenu.getMenu().getId())
                .menuName(orderMenu.getMenu().getName())
                .quantity(orderMenu.getQuantity())
                .price(BigDecimal.valueOf(orderMenu.getPrice()))
                .subtotal(orderMenu.getSubTotal())
                .build();
    }
}

