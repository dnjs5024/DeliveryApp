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

    //주문 수락 ACCEPT
    @PatchMapping("/store/{storeId}/orders/{orderId}/accept")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> acceptOrder(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId) {

        OrderResponseDTO updated = orderService.changeStatus(user.getId(), storeId, orderId, OrderStatus.ACCEPT);

        return ResponseEntity.status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED, updated));
    }
    //주문 거절 REJECT
    @PatchMapping("/store/{storeId}/orders/{orderId}/reject")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> rejectOrder(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId) {

        OrderResponseDTO updated = orderService.changeStatus(user.getId(), storeId, orderId, OrderStatus.REJECT);

        return ResponseEntity.status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED,updated));
    }
    //음식 준비중  PREPARE
    @PatchMapping("/store/{storeId}/orders/{orderId}/prepare")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> prepareOrder(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId) {

        OrderResponseDTO updated = orderService.changeStatus(user.getId(), storeId, orderId, OrderStatus.PREPARE);

        return ResponseEntity.status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED,updated));
    }
//    음식 배달중 DELIVER("배달 중"),
    @PatchMapping("/store/{storeId}/orders/{orderId}/deliver")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> deliverOrder(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId) {

        OrderResponseDTO updated = orderService.changeStatus(user.getId(), storeId, orderId, OrderStatus.DELIVER);

        return ResponseEntity.status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED,updated));
    }

    //음식 주문 완료 COMPLETE("주문 완료"),
    @PatchMapping("/store/{storeId}/orders/{orderId}/complete")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> completeOrder(
            @SessionAttribute("loginUser") SessionUserDto user,
            @PathVariable Long storeId,
            @PathVariable Long orderId) {

        OrderResponseDTO updated = orderService.changeStatus(user.getId(), storeId, orderId, OrderStatus.COMPLETE);

        return ResponseEntity.status(SuccessCode.ORDER_STATUS_UPDATED.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.ORDER_STATUS_UPDATED,updated));
        }
}
