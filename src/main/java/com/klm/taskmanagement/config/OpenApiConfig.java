/**
 * Configuration class for OpenAPI (Swagger) documentation and security setup.
 *
 * - Defines API metadata such as title and version.
 * - Configures JWT Bearer token as the global security scheme for the API.
 * - Enables the "Authorize" button in Swagger UI to accept JWT tokens.
 *
 * This allows secured endpoints to be tested via Swagger by sending
 * the JWT token in the Authorization header as a Bearer token.
 */
package com.klm.taskmanagement.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration to setup JWT Bearer authentication scheme for Swagger UI.
 * Enables global security requirement so all API endpoints can be authorized via JWT token.
 */
@Configuration
public class OpenApiConfig {
    /**
     * Configures the OpenAPI definition for Swagger UI.
     * Adds JWT authentication scheme.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Task Management System API")
                        .version("1.0")
                        .description("API documentation for Task Management System"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)));
    }
}
