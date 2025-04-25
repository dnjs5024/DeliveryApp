package com.example.delivery.domain.order.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderOwnerController {


    //주문 목록 (owner) get -> 페이징 Slice로
    //주문 상세조회 get
    //주문 수락 patch
    //주문 거절 patch
    //음식 주문완료 patch
    // 배달시작 patch
    //배달 완료 patch
}
