package com.example.delivery.domain.user.service;

import com.example.delivery.domain.user.dto.UserResponseDto;
import com.example.delivery.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto signup(String email, String password, User.Role role, String username);
}
