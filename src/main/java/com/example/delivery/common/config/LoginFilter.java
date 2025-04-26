package com.example.delivery.common.config;

import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.delivery.common.constants.BusinessRuleConstants.LOGIN_USER_SESSION_KEY;

@Slf4j
public class LoginFilter implements Filter {

    private static final String STORE_API_PREFIX = "/api/store";
    private static final String[] WHITE_LIST = {"/api/auth/signup", "/api/auth/login"};


    private final UserService userService;

    public LoginFilter(UserService userService) {
        this.userService = userService;
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
                writeResponse(httpResponse,request, ErrorCode.UNAUTHORIZED);
                return;
            }
        }

        User loginUser = null;

        // session == null 일 경우 NullPointerException 발생하므로 조건문 처리
        if (session != null) {
            // 세션에서 SessionUserDto 객체 가져오기
            SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(LOGIN_USER_SESSION_KEY);

            // sessionUserDto 가 존재하는 경우 User 객체로 변환하거나 필요한 작업 수행
            if (sessionUserDto != null) {
                // 이메일을 사용하여 User 엔티티를 조회
                loginUser = userService.findUser((sessionUserDto.getEmail()));
            }
        }

        // 현재 로그인한 유저가 존재하지 않거나, URI : "/api/store"로 시작하고, role : OWNER 가 아닐 경우
        if (loginUser != null) {
            if (requestURI.startsWith(STORE_API_PREFIX) && !loginUser.getRole().equals(User.Role.OWNER)) {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                writeResponse(httpResponse,request, ErrorCode.OWNER_PERMISSION_REQUIRED);
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

    void writeResponse(HttpServletResponse servletResponse, HttpServletRequest request, ErrorCode errorCode) throws IOException {

        servletResponse.setStatus(errorCode.getHttpStatus().value());  // 403 Forbiddencreated_at
        servletResponse.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", LocalDateTime.now().toString());
        responseMap.put("statusCode", errorCode.getHttpStatus().value());
        responseMap.put("message", errorCode.getMessage());
        responseMap.put("path", request.getRequestURI());

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(responseMap);

        servletResponse.getWriter().write(responseJson);
    }


}
