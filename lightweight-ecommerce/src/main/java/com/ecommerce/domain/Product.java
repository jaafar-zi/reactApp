package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "name_key", nullable = false)
    private String nameKey;

    @Column(name = "description_key")
    private String descriptionKey;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ProductAttribute> attributes = new LinkedHashSet<>();

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<ProductAsset> assets = new ArrayList<>();

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToOne(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private ProductPricing pricing;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ─── Helper methods ──────────────────────────────────────

    public void addAttribute(ProductAttribute attribute) {
        attributes.add(attribute);
        attribute.setProduct(this);
    }

    public void removeAttribute(ProductAttribute attribute) {
        attributes.remove(attribute);
        attribute.setProduct(null);
    }

    public void addAsset(ProductAsset asset) {
        assets.add(asset);
        asset.setProduct(this);
    }

    public void removeAsset(ProductAsset asset) {
        assets.remove(asset);
        asset.setProduct(null);
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {
        variants.remove(variant);
        variant.setProduct(null);
    }

    public void setPricingData(ProductPricing pricing) {
        this.pricing = pricing;
        pricing.setProduct(this);
    }

    public boolean hasVariants() {
        return variants != null && !variants.isEmpty();
    }

    // ─── i18n resolution helpers ─────────────────────────────

    public String getResolvedName(String language) {
        return attributes.stream()
                .filter(a -> "name".equals(a.getAttributeKey())
                        && language.equals(a.getLanguage()))
                .findFirst()
                .map(ProductAttribute::getAttributeValue)
                .orElse(nameKey);
    }

    public String getResolvedDescription(String language) {
        return attributes.stream()
                .filter(a -> "description".equals(a.getAttributeKey())
                        && language.equals(a.getLanguage()))
                .findFirst()
                .map(ProductAttribute::getAttributeValue)
                .orElse(descriptionKey);
    }

    public List<String> getResolvedAttributeValues(String key, String language) {
        return attributes.stream()
                .filter(a -> key.equals(a.getAttributeKey())
                        && language.equals(a.getLanguage()))
                .findFirst()
                .map(ProductAttribute::getAttributeValueList)
                .orElse(new ArrayList<>());
    }
}