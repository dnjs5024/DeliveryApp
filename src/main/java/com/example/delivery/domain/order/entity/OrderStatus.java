package com.example.delivery.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    /**
     * PENDING : 주문 요청(손님)
     * ACCEPT : 주문수락
     * REJECT : 주문거절
     * PREPARE : 음식 준비중
     * DELIVER : 배달 중
     * COMPLETE : 주문완료
     *  DELETED : 삭제된 주문 -> 소프트 delete
     */
    PENDING("주문 요청"),
    ACCEPT("주문 수락"),
    REJECT("주문 거절"),
    PREPARE("음식 준비중"),
    DELIVER("배달 중"),
    COMPLETE("주문 완료"),
    DELETED("삭제된 주문");

    private final String description;

    OrderStatus(String description){
        this.description = description;
    }

}
