package com.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShippingRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;
    
    @NotNull(message = "Shipping method is required")
    private String shippingMethod;
    
    @NotNull(message = "Carrier is required")
    private String carrier;
    
    private BigDecimal shippingCost;
    private BigDecimal weight;
    private String specialInstructions;
    private String notes;
}