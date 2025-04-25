package com.example.delivery.domain.order.service;

import com.example.delivery.domain.order.dto.requestDTO.OrderRequestDTO;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.user.dto.SessionUserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface OrderService {
    //고객 주문하기
    OrderResponseDTO createOrder(SessionUserDto sessionUserDto, OrderRequestDTO requestDTO);
    //Owner 주문 조회(페이지)
    Slice<OrderResponseDTO> getStoreOrders(Long userId,Long storeId, Pageable pageable);
    //주문 상세보기
    OrderResponseDTO getDetail(Long userId, Long storeId, Long orderId);
}
