package com.example.delivery.common.config;
import com.example.delivery.domain.user.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter(UserService userService) {
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LoginFilter(userService)); // 필터 등록
        filterRegistrationBean.setOrder(1); //순서1
        filterRegistrationBean.addUrlPatterns("/api/*"); // 전체 URL에 필터 적용

        return filterRegistrationBean;
    }

}
