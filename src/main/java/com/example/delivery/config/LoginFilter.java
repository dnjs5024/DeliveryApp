package com.example.delivery.config;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        log.info("로그인 필터 로직 실행");

        if (!isWhiteList(requestURI)) {
            HttpSession session = request.getSession(false); // 세션이 없으면 null

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
