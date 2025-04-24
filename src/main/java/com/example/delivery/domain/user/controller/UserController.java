package com.example.delivery.domain.user.controller;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;

import com.example.delivery.domain.user.dto.*;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.delivery.domain.user.dto.UserRequestDto;
import com.example.delivery.domain.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> signup(@Valid @RequestBody UserRequestDto dto) {
        UserResponseDto userResponseDto = userService.signup(dto.email(), dto.password(), dto.role(), dto.username());
        return ResponseEntity.status(
                SuccessCode.SIGNUP_SUCCESS.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.SIGNUP_SUCCESS, userResponseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Void>> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletRequest request
    ) {
        // 세션 가져오기
        HttpSession session = request.getSession(false);

        // 세션 있는지 확인
        if (session != null) {
            User existingUser = (User) session.getAttribute("loginUser");

            // 탈퇴한 사용자인지 확인
            if (existingUser != null && existingUser.getWithdrawTime() != null) {
                throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
            }

            // 이미 로그인되어 있을 경우 예외 처리
            throw new CustomException(ErrorCode.ALREADY_LOGGED_IN);
        }

        // 로그인 처리
        User user = userService.login(dto.getEmail(),dto.getPassword());

        // 세션 생성 및 로그인 정보 저장
        // User 엔티티는 테이블과 매핑되기 때문에 세션에 User 를 직접 저장하지 않고 SessionUserDto 를 저장한다.
        SessionUserDto sessionUserDto = new SessionUserDto(user.getId(), user.getEmail(), user.getRole());
        
        // 이전 세션 체크후 제거
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate(); // 기존 세션 제거
        }
        // 새 세션 생성
        request.getSession(true).setAttribute("loginUser", sessionUserDto);

        return ResponseEntity.status(
                SuccessCode.LOGIN_SUCCESS.getHttpStatus()).body(
                        ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS));
    }

    @PutMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            HttpServletRequest request
    ) {
        userService.logout(request);

        return ResponseEntity.status(
                SuccessCode.LOGOUT_SUCCESS.getHttpStatus()).body(
                        ApiResponseDto.success(SuccessCode.LOGOUT_SUCCESS));

    }

    @PatchMapping("/withdraw")
    public ResponseEntity<ApiResponseDto<Void>> withdraw(
            HttpServletRequest request,
            @RequestBody LoginRequestDto dto
    ) {
        userService.withdraw(request, dto);

        return ResponseEntity.status(
                SuccessCode.WITHDRAWAL_SUCCESS.getHttpStatus()).body(
                ApiResponseDto.success(SuccessCode.WITHDRAWAL_SUCCESS));
    }


}
