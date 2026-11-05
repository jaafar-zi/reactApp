package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.domain.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private String orderNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[\\d\\s-]{8,15}$", message = "Invalid phone number format")
    private String customerPhone;

    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank
    private String customerName;

    private OrderStatus status;

    @NotEmpty
    private List<OrderItemDTO> items;

    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal tax;
    private String discountCode;
    private BigDecimal discountAmount;
    private String appliedDiscountCode;
    private BigDecimal total;
    private String currency;
    private String shippingAddress;
    private String billingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean deferConfirmationEmail;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {
        private Long productId;
        private Long variantId;
        private String productName;
        private String selectedSize;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}