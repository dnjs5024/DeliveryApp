package com.example.delivery.domain.user.service;

import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.config.PasswordEncoder;
import com.example.delivery.domain.user.dto.UserResponseDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
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

}
