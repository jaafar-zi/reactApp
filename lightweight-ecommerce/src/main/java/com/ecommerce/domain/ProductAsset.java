package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String assetType;

    @Column(nullable = false)
    private String url;

    private String altText;

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "asset_link_id")
    private Long assetLinkId;
}