package com.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Name key is required")
    private String nameKey;

    private String descriptionKey;

    @Builder.Default
    private Boolean active = true;

    @Valid
    @Builder.Default
    private List<CreateAttributeRequest> attributes = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CreateAssetRequest> assets = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CreateVariantRequest> variants = new ArrayList<>();

    @Valid
    private CreatePricingRequest pricing;

    // ─── Nested requests ──────────────────────────────────────

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateAttributeRequest {

        @NotBlank(message = "Attribute key is required")
        private String attributeKey;

        @NotBlank(message = "Language is required")
        private String language;

        @Builder.Default
        private List<String> values = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateAssetRequest {

        @NotBlank(message = "Asset type is required")
        private String assetType;

        @NotBlank(message = "URL is required")
        private String url;

        private String altText;

        @Builder.Default
        private Integer displayOrder = 0;

        @Builder.Default
        private Boolean isPrimary = false;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateVariantRequest {

        @NotBlank(message = "Size is required")
        private String size;

        private Integer initialStock;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePricingRequest {

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.0", inclusive = false)
        private BigDecimal basePrice;

        private BigDecimal salePrice;

        @Builder.Default
        private String currency = "USD";

        @Builder.Default
        private BigDecimal taxRate = BigDecimal.ZERO;
    }
}