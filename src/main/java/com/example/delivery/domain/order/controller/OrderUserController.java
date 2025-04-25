package com.example.delivery.domain.order.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.requestDTO.OrderRequestDTO;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.order.service.OrderService;
import com.example.delivery.domain.user.dto.SessionUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderUserController {

    private final OrderService orderService;

    /**
     * 주문하기 (유저)
     */
    @PostMapping("/orders")
    public ResponseEntity<ApiResponseDto<OrderResponseDTO>> order(
            @SessionAttribute(name = "loginUser") SessionUserDto sessionUserDto,
            @RequestBody @Valid OrderRequestDTO requestDTO
    ) {
        //주문 생성
        OrderResponseDTO order = orderService.createOrder(sessionUserDto, requestDTO);

        return ResponseEntity.status(SuccessCode.ORDER_CREATED.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.ORDER_CREATED,order));
    }

}
