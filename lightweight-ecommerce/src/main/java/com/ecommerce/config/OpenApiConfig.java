// src/main/java/com/ecommerce/config/OpenApiConfig.java
package com.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @Value("${app.company-name:E-commerce Platform}")
    private String companyName;
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        // Define security scheme for Basic Auth
        SecurityScheme basicAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .name("basicAuth")
                .description("Basic Authentication using username and password");
        
        // Security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("basicAuth");
        
        return new OpenAPI()
                .info(new Info()
                        .title(companyName + " API")
                        .description("RESTful API for E-commerce Platform with multilingual support, " +
                                "product management, order processing, inventory tracking, and shipping management.")
                        .version(appVersion)
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@yourcompany.com")
                                .url("https://yourcompany.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                        .termsOfService("https://yourcompany.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.yourcompany.com")
                                .description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", basicAuthScheme))
                .addSecurityItem(securityRequirement);
    }
}