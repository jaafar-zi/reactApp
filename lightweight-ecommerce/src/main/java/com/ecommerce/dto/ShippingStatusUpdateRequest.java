package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingStatusUpdateRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String description;
    private String notes;
    private String updatedBy;
}