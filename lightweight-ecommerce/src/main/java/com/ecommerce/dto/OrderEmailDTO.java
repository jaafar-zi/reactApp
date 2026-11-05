package com.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Flattened DTO for email template rendering.
 * Avoids lazy-loading issues and i18n resolution in templates.
 */
@Data
@Builder
public class OrderEmailDTO {

    private String name;
    private String email;

    private String orderNumber;
    private String orderDate;
    private String deliveryAddress;

    private BigDecimal subtotal;
    private BigDecimal shipping;
    private BigDecimal total;

    private String discountCode;
    private BigDecimal discountAmount;


    private List<OrderItemEmailDTO> orderItems;

    private String companyName;
    private String supportEmail;
    private String frontendUrl;

    @Data
    @Builder
    public static class OrderItemEmailDTO {
        private String productName;
        private String imageUrl;
        private Integer quantity;
        private BigDecimal price;
    }
}