package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_pricing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(length = 3, nullable = false)
    @Builder.Default
    private String currency = "USD";

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    // ─── Helpers ─────────────────────────────────────────────

    public BigDecimal getEffectivePrice() {
        return (salePrice != null
                && salePrice.compareTo(BigDecimal.ZERO) > 0
                && salePrice.compareTo(basePrice) < 0)
                ? salePrice
                : basePrice;
    }

    public boolean isOnSale() {
        return salePrice != null
                && salePrice.compareTo(BigDecimal.ZERO) > 0
                && salePrice.compareTo(basePrice) < 0;
    }
}