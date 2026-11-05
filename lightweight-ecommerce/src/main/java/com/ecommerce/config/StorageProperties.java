package com.ecommerce.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private String baseDir = "./uploads"; 

    private List<String> allowedTypes = List.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private Long maxFileSize = 10_485_760L;
}