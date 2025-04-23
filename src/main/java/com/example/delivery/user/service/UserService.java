package com.example.delivery.user.service;

import com.example.delivery.user.dto.LoginRequestDto;
import com.example.delivery.user.dto.UserRequestDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto signup(String email, String password, User.Role role, String username);
    User login(String email, String password);
    void logout(HttpServletRequest request);
    void withdraw(HttpServletRequest request,LoginRequestDto dto);
}
