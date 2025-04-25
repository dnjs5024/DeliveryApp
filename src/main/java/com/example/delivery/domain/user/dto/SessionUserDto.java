package com.example.delivery.domain.user.dto;

import com.example.delivery.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SessionUserDto {
    private final Long id;
    private final String email;
    private final User.Role role;

    public SessionUserDto(Long id, String email, User.Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
