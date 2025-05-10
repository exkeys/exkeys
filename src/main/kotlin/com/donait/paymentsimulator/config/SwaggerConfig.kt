package com.donait.paymentsimulator.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Payment Simulator API")
                    .description("결제 시뮬레이터 API 문서")
                    .version("v1.0.0")
                    .contact(
                        Contact()
                            .name("Payment Simulator Team")
                            .email("support@example.com")
                    )
            )
            .schemaRequirement("bearerAuth", SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
            )
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
    }
} 