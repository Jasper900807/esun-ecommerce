package com.esun.ecommerce.config;

import com.esun.ecommerce.util.XssFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web 安全設定
 * 
 * 註冊 XSS 過濾器
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final XssFilter xssFilter;

    /**
     * 註冊 XSS 過濾器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(xssFilter);
        registration.addUrlPatterns("/*");  // 對所有路徑生效
        registration.setName("xssFilter");
        registration.setOrder(1);  // 優先順序
        return registration;
    }
}