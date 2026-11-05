package com.ecommerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadataResponse {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String downloadUrl;
    private String contentType;
    private long fileSize;
    private String category;
    private String checksum;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
}