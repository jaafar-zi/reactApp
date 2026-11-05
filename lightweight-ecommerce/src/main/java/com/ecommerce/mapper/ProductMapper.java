package com.ecommerce.mapper;

import com.ecommerce.domain.*;
import com.ecommerce.dto.*;
import com.ecommerce.dto.CreateProductRequest.*;
import com.ecommerce.service.AttributeKeyTranslationService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ProductMapper {

    private final AttributeKeyTranslationService translationService;

    public ProductMapper(AttributeKeyTranslationService translationService) {
        this.translationService = translationService;
    }

    // ─── toDTO ───────────────────────────────────────────────

    public ProductDTO toDTO(Product product, String language) {
        if (product == null) return null;

        // Fetch translation map for current language
        Map<String, String> keyLabels = translationService.getTranslationMap(language);

        return ProductDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .name(product.getResolvedName(language))
                .description(product.getResolvedDescription(language))
                .pricing(toPricingDTO(product.getPricing()))
                .attributes(mapAttributes(product.getAttributes(), keyLabels))
                .assets(mapAssets(product.getAssets()))
                .variants(mapVariants(product.getVariants()))
                .hasVariants(product.hasVariants())
                .inventory(null)
                .build();
    }

    // ─── Variants ─────────────────────────────────────────────

    private List<ProductVariantDTO> mapVariants(List<ProductVariant> variants) {
        if (variants == null || variants.isEmpty()) return Collections.emptyList();
        return variants.stream()
                .map(v -> ProductVariantDTO.builder()
                        .id(v.getId())
                        .sku(v.getSku())
                        .size(v.getSize())
                        .active(v.getActive())
                        .build())
                .toList();
    }

    // ─── Attributes ───────────────────────────────────────────

    private List<ProductAttributeDTO> mapAttributes(
            Collection<ProductAttribute> attributes,
            Map<String, String> keyLabels
    ) {
        if (attributes == null || attributes.isEmpty()) return Collections.emptyList();
        return attributes.stream()
                .map(attr -> toAttributeDTO(attr, keyLabels))
                .toList();
    }

    private ProductAttributeDTO toAttributeDTO(
            ProductAttribute attribute,
            Map<String, String> keyLabels
    ) {
        String key   = attribute.getAttributeKey();
        String label = keyLabels.getOrDefault(key, formatFallbackLabel(key));

        return ProductAttributeDTO.builder()
                .id(attribute.getId())
                .attributeKey(key)
                .attributeKeyLabel(label)
                .language(attribute.getLanguage())
                .attributeValues(mapAttributeValues(attribute.getAttributeValues()))
                .build();
    }

    private List<AttributeValueDTO> mapAttributeValues(Collection<AttributeValue> values) {
        if (values == null || values.isEmpty()) return Collections.emptyList();
        return values.stream()
                .map(v -> AttributeValueDTO.builder()
                        .id(v.getId())
                        .value(v.getValue())
                        .build())
                .toList();
    }

    /**
     * Fallback: converts 'product_details' → 'Product Details'
     */
    private String formatFallbackLabel(String key) {
        if (key == null || key.isEmpty()) return "";
        return key.replace('_', ' ')
                  .replace('-', ' ')
                  .substring(0, 1).toUpperCase()
                + key.replace('_', ' ')
                     .replace('-', ' ')
                     .substring(1);
    }

    // ─── Assets ───────────────────────────────────────────────

    private List<ProductAssetDTO> mapAssets(List<ProductAsset> assets) {
        if (assets == null || assets.isEmpty()) return Collections.emptyList();
        return assets.stream()
                .map(a -> ProductAssetDTO.builder()
                        .id(a.getId())
                        .assetType(a.getAssetType())
                        .url(a.getUrl())
                        .altText(a.getAltText())
                        .displayOrder(a.getDisplayOrder())
                        .isPrimary(a.getIsPrimary())
                        .build())
                .toList();
    }

    // ─── Pricing ──────────────────────────────────────────────

    private ProductPricingDTO toPricingDTO(ProductPricing pricing) {
        if (pricing == null) return null;
        return ProductPricingDTO.builder()
                .id(pricing.getId())
                .basePrice(pricing.getBasePrice())
                .salePrice(pricing.getSalePrice())
                .effectivePrice(pricing.getEffectivePrice())
                .currency(pricing.getCurrency())
                .taxRate(pricing.getTaxRate())
                .onSale(pricing.isOnSale())
                .build();
    }

    // ─── CreateRequest → Entity ───────────────────────────────

    public Product toEntity(CreateProductRequest request) {
        Product product = Product.builder()
                .sku(request.getSku())
                .nameKey(request.getNameKey())
                .descriptionKey(request.getDescriptionKey())
                .active(request.getActive() != null ? request.getActive() : Boolean.TRUE)
                .build();

        if (request.getAttributes() != null) {
            request.getAttributes().forEach(attrReq ->
                product.addAttribute(buildAttribute(attrReq))
            );
        }

        if (request.getAssets() != null) {
            request.getAssets().forEach(assetReq ->
                product.addAsset(buildAsset(assetReq))
            );
        }

        if (request.getVariants() != null) {
            request.getVariants().forEach(varReq ->
                product.addVariant(buildVariant(varReq, request.getSku()))
            );
        }

        if (request.getPricing() != null) {
            product.setPricingData(buildPricing(request.getPricing()));
        }

        return product;
    }

    private ProductVariant buildVariant(CreateVariantRequest req, String productSku) {
        return ProductVariant.builder()
                .sku(productSku + "-" + req.getSize().toUpperCase())
                .size(req.getSize().toUpperCase())
                .active(true)
                .build();
    }

    private ProductAttribute buildAttribute(CreateAttributeRequest req) {
        ProductAttribute attribute = ProductAttribute.builder()
                .attributeKey(req.getAttributeKey())
                .language(req.getLanguage())
                .build();

        if (req.getValues() != null) {
            req.getValues().forEach(val ->
                attribute.addAttributeValue(
                    AttributeValue.builder()
                            .value(val)
                            .build()
                )
            );
        }
        return attribute;
    }

    private ProductAsset buildAsset(CreateAssetRequest req) {
        return ProductAsset.builder()
                .assetType(req.getAssetType())
                .url(req.getUrl())
                .altText(req.getAltText())
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .isPrimary(req.getIsPrimary() != null ? req.getIsPrimary() : Boolean.FALSE)
                .build();
    }

    private ProductPricing buildPricing(CreatePricingRequest req) {
        return ProductPricing.builder()
                .basePrice(req.getBasePrice())
                .salePrice(req.getSalePrice())
                .currency(req.getCurrency() != null ? req.getCurrency() : "USD")
                .taxRate(req.getTaxRate() != null ? req.getTaxRate() : BigDecimal.ZERO)
                .build();
    }

    // ─── UpdateRequest → existing Entity ──────────────────────

    public void updateEntityFromRequest(UpdateProductRequest request, Product existing) {
        if (request.getNameKey() != null) {
            existing.setNameKey(request.getNameKey());
        }
        if (request.getDescriptionKey() != null) {
            existing.setDescriptionKey(request.getDescriptionKey());
        }
        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            existing.getAttributes().clear();
            request.getAttributes().forEach(attrReq ->
                existing.addAttribute(buildAttribute(attrReq))
            );
        }
        if (request.getAssets() != null && !request.getAssets().isEmpty()) {
            existing.getAssets().clear();
            request.getAssets().forEach(assetReq ->
                existing.addAsset(buildAsset(assetReq))
            );
        }
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            existing.getVariants().clear();
            request.getVariants().forEach(varReq ->
                existing.addVariant(buildVariant(varReq, existing.getSku()))
            );
        }
        if (request.getPricing() != null) {
            if (existing.getPricing() != null) {
                if (request.getPricing().getBasePrice() != null) {
                    existing.getPricing().setBasePrice(request.getPricing().getBasePrice());
                }
                existing.getPricing().setSalePrice(request.getPricing().getSalePrice());
                if (request.getPricing().getCurrency() != null) {
                    existing.getPricing().setCurrency(request.getPricing().getCurrency());
                }
                if (request.getPricing().getTaxRate() != null) {
                    existing.getPricing().setTaxRate(request.getPricing().getTaxRate());
                }
            } else {
                existing.setPricingData(buildPricing(request.getPricing()));
            }
        }
    }
}