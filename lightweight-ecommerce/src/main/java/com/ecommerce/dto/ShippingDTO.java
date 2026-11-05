package com.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDTO {
    private Long id;
    private String trackingNumber;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    private String orderNumber;
    
    @NotNull(message = "Shipping address is required")
    private AddressDTO shippingAddress;
    
    @NotNull(message = "Shipping method is required")
    private String shippingMethod;
    
    private String currentStatus;
    
    @NotNull(message = "Carrier is required")
    private String carrier;
    
    private BigDecimal shippingCost;
    private BigDecimal weight;
    
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime shippedDate;
    
    private String specialInstructions;
    private String notes;
    
    private List<ShippingStatusDTO> statusHistory;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}