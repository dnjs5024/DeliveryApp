package com.example.delivery.domain.user.service;

import com.example.delivery.common.exception.base.BadRequestException;
import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.config.PasswordEncoder;
import com.example.delivery.domain.user.dto.LoginRequestDto;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.user.dto.UserResponseDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
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
    public void withdraw(HttpServletRequest request, LoginRequestDto loginRequestDto) {

        // 요청 데이터에서 세션을 가져옴 (없으면 null 반환)
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 세션에서 SessionUserDto 가져오기
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute("loginUser");

        if (sessionUserDto == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 세션에서 User 객체 가져오기
        User loginUser = userRepository.findUserByEmailOrElseThrow(sessionUserDto.getEmail());

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), loginUser.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // 이미 탈퇴한 사용자일 경우 처리
        if (loginUser.getWithdrawTime() != null) {
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
        }

        // 탈퇴 처리
        loginUser.withdraw();
        request.getSession().invalidate();
    }

    @Override
    public User findUser(String email) {
        return userRepository.findUserByEmailOrElseThrow(email);
    }


}