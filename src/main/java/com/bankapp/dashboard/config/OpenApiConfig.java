package com.bankapp.dashboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Dashboard API")
                        .description("APIs for accounts, transactions, payments, analytics, and users")
                        .version("v1.0"));
    }
}
