package com.ecommerce.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    private String nameKey;
    private String descriptionKey;
    private Boolean active;

    @Valid
    @Builder.Default
    private List<CreateProductRequest.CreateAttributeRequest> attributes = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CreateProductRequest.CreateAssetRequest> assets = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CreateProductRequest.CreateVariantRequest> variants = new ArrayList<>();

    @Valid
    private CreateProductRequest.CreatePricingRequest pricing;
}