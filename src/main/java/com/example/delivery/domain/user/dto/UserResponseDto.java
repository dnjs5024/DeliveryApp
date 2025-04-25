package com.example.delivery.domain.user.dto;

import com.example.delivery.domain.user.entity.User;


public record UserResponseDto(String email, User.Role role, String username) {
}
