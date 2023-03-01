package com.achao.org.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author achao
 * http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("组织增删改查接口-测试审计日志"));
    }
}
