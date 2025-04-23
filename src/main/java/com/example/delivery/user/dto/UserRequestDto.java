package com.example.delivery.user.dto;

import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserRequestDto {

    @NotNull(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 잘못되었습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=:;,.<>?]).{8,}$", message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
    private final String password;

    @NotNull(message = "사용자 역할은 필수입니다.")
    private final User.Role role;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    private final String username;

    public UserRequestDto(String email, String password, User.Role role, String username) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }
}
