// src/main/java/com/yourcompany/ecommerce/dto/ShippingStatusDTO.java
package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingStatusDTO {
    private Long id;
    
    @NotNull(message = "Shipping ID is required")
    private Long shippingId;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    private String description;
    private String notes;
    private String updatedBy;
    
    private LocalDateTime createdAt;
}