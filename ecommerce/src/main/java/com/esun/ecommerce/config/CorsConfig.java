package com.esun.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS 跨域設定
 * 
 * 允許前端（Vue.js）從不同的 origin 呼叫後端 API
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允許的來源（前端 URL）
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",     // Vite 預設埠
                "http://localhost:3000",     // Vue CLI 預設埠
                "http://localhost:8081"      // 其他可能的前端埠
        ));
        
        // 允許的 HTTP 方法
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        // 允許的請求頭
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // 是否允許發送 Cookie
        config.setAllowCredentials(true);
        
        // 預檢請求的有效期（秒）
        config.setMaxAge(3600L);
        
        // 註冊 CORS 設定
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}