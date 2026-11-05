package com.ecommerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
@Validated 
public class AppProperties {

    private String defaultLanguage = "en";

    @NotEmpty(message = "At least one supported language must be configured")
    private List<String> supportedLanguages = List.of("en", "fr", "es", "de");

    @NotBlank(message = "Frontend URL must be configured")
    private String frontendUrl;

    @NotBlank(message = "Company name must be configured")
    private String companyName = "Jawista Club";

    @Email(message = "Support email must be a valid email address")
    @NotBlank(message = "Support email must be configured")
    private String supportEmail = "jawistaclub@gmail.com";

    @Valid 
    private Mail mail = new Mail();

    @Valid 
    private Security security = new Security();

    // =========================================================
    // NESTED: Mail
    // =========================================================

    @Data
    public static class Mail {

        @Email(message = "From email must be a valid email address")
        @NotBlank(message = "Mail from-email must be configured")
        private String fromEmail;

        @NotBlank(message = "Mail from-name must be configured")
        private String fromName;

        private boolean enabled = true;
    }

    // =========================================================
    // NESTED: Security
    // =========================================================

    @Data
    public static class Security {

        @NotBlank(message = "JWT secret must be configured")
        private String jwtSecret;

        @Positive(message = "JWT expiration must be a positive value")
        private long jwtExpirationMs = 86400000L; 
    }
}