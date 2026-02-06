package com.esun.ecommerce.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * XSS 過濾器
 * 
 * 攔截所有請求，過濾潛在的 XSS 攻擊腳本
 */
@Component
@Slf4j
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("XSS 過濾器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // 包裝請求，過濾 XSS
        XssRequestWrapper wrappedRequest = new XssRequestWrapper(httpRequest);
        
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        log.info("XSS 過濾器銷毀");
    }
}