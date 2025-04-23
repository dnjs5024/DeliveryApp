package com.example.delivery.user.dto;

import com.example.delivery.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final String email;
    private final User.Role role;
    private final String username;

    public UserResponseDto(String email, User.Role role, String username) {
        this.email = email;
        this.role = role;
        this.username = username;
    }
}
