package com.example.delivery.domain.user.dto;

import lombok.Getter;

@Getter
public class SessionUserDto {
    private final String email;
    private final String password;

    public SessionUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
