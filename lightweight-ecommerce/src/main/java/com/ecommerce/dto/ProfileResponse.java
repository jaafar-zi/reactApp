package com.ecommerce.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    String role,
    LocalDateTime createdAt
) {}