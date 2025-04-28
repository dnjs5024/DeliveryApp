package com.example.delivery.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;


@Getter
@Builder
@Jacksonized
public class LoginRequestDto {
    private final String email;
    private final String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
