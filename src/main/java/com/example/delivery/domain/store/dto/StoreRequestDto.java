package com.example.delivery.domain.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
// record는 데이터객체(불변객체)를 만들기 위한 것
// 모든 필드에 대한 getter, toString(), equals(), hashCode(), 생성자를 자동 생성
public record StoreRequestDto(
        @NotBlank String name,
        @NotNull LocalTime openTime,
        @NotNull LocalTime closeTime,
        @Min(10000) int minOrderPrice
        ) {
}
