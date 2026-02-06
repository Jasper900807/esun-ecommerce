package com.esun.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger API 文檔設定
 * 
 * 訪問：http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E.SUN E-Commerce API")
                        .version("1.0.0")
                        .description("玉山銀行電商購物中心系統 API 文檔")
                        .contact(new Contact()
                                .name("Jasper")
                                .email("your-email@example.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("本地開發環境")
                ));
    }
}