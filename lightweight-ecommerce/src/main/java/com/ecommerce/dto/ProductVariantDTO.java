package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {

    private Long id;
    private String sku;
    private String size;
    private Boolean active;
    private VariantInventoryDTO inventory;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VariantInventoryDTO {
        private Integer quantity;
        private Integer availableQuantity;
        private Boolean inStock;
        private Boolean lowStock;
    }
}