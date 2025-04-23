package com.example.delivery.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotNull(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 잘못되었습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=:;,.<>?]).{8,}$", message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
    private final String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
