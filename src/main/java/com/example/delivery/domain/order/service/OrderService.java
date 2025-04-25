package com.example.delivery.domain.order.service;

import com.example.delivery.domain.order.dto.requestDTO.OrderRequestDTO;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface OrderService {
    //고객 주문하기
    OrderResponseDTO createOrder(Long userId, OrderRequestDTO requestDTO);
    //Owner 주문 조회(페이지)
    Slice<OrderResponseDTO> getStoreOrders(Long storeId, Pageable pageable);
}
