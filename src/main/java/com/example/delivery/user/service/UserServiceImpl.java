package com.example.delivery.user.service;

import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.config.PasswordEncoder;
import com.example.delivery.user.dto.LoginRequestDto;
import com.example.delivery.user.dto.UserRequestDto;
import com.example.delivery.user.dto.UserResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto signup(String email, String password, User.Role role, String username) {

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, role, username);
        User signUser = userRepository.save(user);

        return new UserResponseDto(signUser.getEmail(), signUser.getRole(), signUser.getUsername());
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findUserByEmailOrElseThrow(email);

        // 이미 탈퇴한 사용자 체크
        if (user.getWithdrawTime() != null) {
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return user;
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @Override
    public void withdraw(HttpServletRequest request, LoginRequestDto dto) {

        User user = userRepository.findUserByEmailOrElseThrow(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(),user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        };

        if (user.getWithdrawTime() != null) {
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
        }
        user.withdraw();
        request.getSession().invalidate();
    }


}
