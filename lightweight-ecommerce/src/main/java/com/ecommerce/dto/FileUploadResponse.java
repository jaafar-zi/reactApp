package com.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileUploadResponse {
    private String publicId;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String url;
    private LocalDateTime uploadedAt;
}