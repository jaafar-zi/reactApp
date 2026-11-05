package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_key", nullable = false)
    private String attributeKey;

    @Column(nullable = false)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(
        mappedBy = "productAttribute",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Builder.Default
    private Set<AttributeValue> attributeValues = new LinkedHashSet<>();

    // ─── Helper methods ──────────────────────────────────────

    public void addAttributeValue(AttributeValue value) {
        attributeValues.add(value);
        value.setProductAttribute(this);
    }

    public void removeAttributeValue(AttributeValue value) {
        attributeValues.remove(value);
        value.setProductAttribute(null);
    }

    public String getAttributeValue() {
        return attributeValues.stream()
                .map(AttributeValue::getValue)
                .findFirst()
                .orElse(null);
    }

    public List<String> getAttributeValueList() {
        return attributeValues.stream()
                .map(AttributeValue::getValue)
                .toList();
    }
}