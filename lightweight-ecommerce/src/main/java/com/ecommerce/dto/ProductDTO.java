package com.ecommerce.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String sku;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String name;
    private String description;

    private ProductPricingDTO pricing;

    @Builder.Default
    private List<ProductAttributeDTO> attributes = new ArrayList<>();

    @Builder.Default
    private List<ProductAssetDTO> assets = new ArrayList<>();

    @Builder.Default
    private List<ProductVariantDTO> variants = new ArrayList<>();

    private InventoryDTO inventory;

    private Boolean hasVariants;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryDTO {
        private Integer quantity;
        private Integer availableQuantity;
        private Boolean inStock;
        private Boolean lowStock;
    }
}