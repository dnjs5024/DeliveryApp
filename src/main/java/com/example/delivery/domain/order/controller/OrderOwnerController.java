package com.example.delivery.domain.order.controller;


import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.service.OrderService;
import com.example.delivery.domain.user.dto.SessionUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owner")
public class OrderOwnerController {

    private final OrderService orderService;

    //주문 목록 (owner)-> 페이징 Slice,hasNext로 꺼내기
    @GetMapping("/store/{storeId}/orders")
    public ResponseEntity<ApiResponseDto<Slice<OrderResponseDTO>>> getList(
            @SessionAttribute(name = "loginUser") SessionUserDto sessionUserDto,
            @PathVariable Long storeId,
            Pageable pageable) {
        Long userId = sessionUserDto.getId();
        Slice<OrderResponseDTO> slice =
                orderService.getStoreOrders(userId, storeId, pageable);

        return ResponseEntity.status(SuccessCode.ORDER_PAGING_SUCCESS.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.ORDER_PAGING_SUCCESS, slice));
    }

    //주문 상세조회  -> 유저정보를 더 추가하면 dto새로파서
    @GetMapping("/store/{storeId}/orders/{orderId}")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> getOrder(@SessionAttribute(name = "loginUser") SessionUserDto sessionUserDto, @PathVariable Long storeId, @PathVariable Long orderId) {
        Long userId = sessionUserDto.getId();

        OrderResponseDTO detail = orderService.getDetail(userId, storeId, orderId);
        return ResponseEntity.status(SuccessCode.OK.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.OK, detail));
    }
    @PatchMapping("/store/{storeId}/orders/{orderId}/status/{status}")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> updateStatus(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @PathVariable OrderStatus status
    ) {
        OrderResponseDTO updated =
                orderService.changeStatus(user.getId(), storeId, orderId, status);

        return ResponseEntity
                .status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED, updated));
    }
}

