package com.example.delivery.user.service;

import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto signup(String email, String password, User.Role role, String username);
}
