package com.example.delivery.domain.user.service;

import com.example.delivery.domain.user.dto.LoginRequestDto;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.user.dto.UserResponseDto;
import com.example.delivery.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto signup(String email, String password, User.Role role, String username);
    User login(String email, String password);
    void logout(HttpServletRequest request);
    void withdraw(HttpServletRequest request, LoginRequestDto loginRequestDto);

    User findUser(String email);
}

}
