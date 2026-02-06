package com.esun.ecommerce.config;

import com.esun.ecommerce.util.XssStringDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 設定
 * 
 * 1. 註冊 XSS 過濾的字串反序列化器
 * 2. 支援 Java 8 時間類型（LocalDateTime）
 */
@Configuration
public class JacksonConfig {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // ========== 1. 註冊 XSS 過濾器 ==========
        SimpleModule xssModule = new SimpleModule();
        xssModule.addDeserializer(String.class, new XssStringDeserializer());
        mapper.registerModule(xssModule);
        
        // ========== 2. 註冊 Java 8 時間模組 ==========
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // LocalDateTime 序列化格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        
        mapper.registerModule(javaTimeModule);
        
        // ========== 3. 其他設定 ==========
        // 忽略未知屬性
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // 允許空 Bean
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        // 時區設定
        mapper.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Taipei"));
        
        return mapper;
    }
}