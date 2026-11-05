package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

/**
 * All fields are optional — only non-null fields will be updated.
 */
@Builder
public record UpdateProfileRequest(

    String firstName,

    String lastName,

    @Email(message = "Invalid email format")
    String email
) {}