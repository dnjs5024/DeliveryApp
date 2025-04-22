package com.example.delivery.user.controller;

import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.user.dto.UserRequestDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.service.UserService;
import jakarta.servlet.http.HttpServlet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    UserService userService;

    @PostMapping("/signup")
    ApiResponseDto<UserResponseDto> signup(
            @RequestBody UserRequestDto dto,
            HttpServlet httpServlet
    ) {
        UserResponseDto userService.signup();
    }


}
