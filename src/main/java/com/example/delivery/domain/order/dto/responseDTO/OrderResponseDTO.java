package com.example.delivery.domain.order.dto.responseDTO;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDTO {
    private final Long orderId;
    private final Long storeId;
    private final String storeName;
    private final String requestMessage;
    private final OrderStatus orderStatus;
    private final BigDecimal totalPrice;
    private final LocalDateTime orderedAt;
    private final List<OrderMenuResponseDTO> menus;


    public static OrderResponseDTO toDTO(Order order) {
        List<OrderMenuResponseDTO> menuList = order.getOrderMenuList().stream()
                .map(OrderMenuResponseDTO::toDTO)
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .storeId(order.getStore().getId())
                .storeName(order.getStore().getName())
                .requestMessage(order.getRequestMessage())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getCreatedAt())
                .menus(menuList)
                .build();
    }
}

