package com.example.delivery.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// 불변 객체 Dto -> record 클래스 선언
public record LoginRequestDto(
        @NotNull(message = "이메일은 필수입니다.") @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 잘못되었습니다.") String email,
        @NotBlank(message = "비밀번호는 필수입니다.") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=:;,.<>?]).{8,}$", message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.") String password) {
}
