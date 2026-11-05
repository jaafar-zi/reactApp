package com.ecommerce.dto;

import com.ecommerce.domain.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDiscountCodeRequest {

    @NotBlank
    private String userPrefix;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;

    private Integer maxUses;

    private Integer maxUsesPerCustomer;

    private LocalDateTime expiresAt;
}