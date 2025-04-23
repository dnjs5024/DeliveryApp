package com.example.delivery.domain.user.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.user.dto.UserRequestDto;
import com.example.delivery.domain.user.dto.UserResponseDto;
import com.example.delivery.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> signup(@Valid @RequestBody UserRequestDto dto)
    {
        UserResponseDto userResponseDto = userService.signup(dto.getEmail(), dto.getPassword(), dto.getRole(), dto.getUsername());
        return ResponseEntity.status(
                SuccessCode.SIGNUP_SUCCESS.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.SIGNUP_SUCCESS, userResponseDto));
    }




}
