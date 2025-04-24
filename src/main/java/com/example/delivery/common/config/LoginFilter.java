package com.example.delivery.common.config;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/api/auth/signup", "/api/auth/login"};
    private final UserRepository userRepository;

    public LoginFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        log.info("로그인 필터 로직 실행");

        // 로그인 세션있으면 세션 반환, 없으면 null 반환
        HttpSession session = request.getSession(false);

        // 화이트리스트에 존재하지 않는 URI 요청 처리 -> 로그인 권한 필요한 요청들 처리
        if (!isWhiteList(requestURI)) {
            if (session == null || session.getAttribute("loginUser") == null) {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");

                Map<String, Object> responseMap = new LinkedHashMap<>();
                responseMap.put("timestamp", LocalDateTime.now().toString());
                responseMap.put("statusCode", 401);
                responseMap.put("message", "인증되지 않은 사용자입니다.");
                responseMap.put("path", request.getRequestURI());

                ObjectMapper objectMapper = new ObjectMapper();
                String responseJson = objectMapper.writeValueAsString(responseMap);

                httpResponse.getWriter().write(responseJson);
                return;
            }
        }

        User loginUser = null;

        // session == null 일 경우 NullPointerException 발생하므로 조건문 처리
        if (session != null) {
            // 세션에서 SessionUserDto 객체 가져오기
            SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute("loginUser");

            // sessionUserDto가 존재하는 경우 User 객체로 변환하거나 필요한 작업 수행
            if (sessionUserDto != null) {
                // 이메일을 사용하여 User 엔티티를 조회
                loginUser = userRepository.findUserByEmailOrElseThrow(sessionUserDto.getEmail());
            }
        }

        // 현재 로그인한 유저가 존재하지 않거나, URI가 "/api/store"로 시작하고, 권한이 OWNER가 아닐 경우
        if (loginUser != null) {
            if (requestURI.startsWith("/api/store") && !loginUser.getRole().equals(User.Role.OWNER)) {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
                httpResponse.setContentType("application/json;charset=UTF-8");

                Map<String, Object> responseMap = new LinkedHashMap<>();
                responseMap.put("timestamp", LocalDateTime.now().toString());
                responseMap.put("statusCode", 403);
                responseMap.put("message", "사장님 권한이 필요합니다.");
                responseMap.put("path", request.getRequestURI());

                ObjectMapper objectMapper = new ObjectMapper();
                String responseJson = objectMapper.writeValueAsString(responseMap);

                httpResponse.getWriter().write(responseJson);
                return;
            }
        }

        // WHITELIST 의 URI 이거나 특정 접근 권한(로그인 권한, 가게 권한 등)이 있을 경우 서블릿(Controller)으로 넘김
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public boolean isWhiteList(String requestURI) {
        for (String s : WHITE_LIST) {
            if (requestURI.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


}
