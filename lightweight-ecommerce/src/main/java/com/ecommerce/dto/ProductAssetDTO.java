package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAssetDTO {

    private Long id;
    private String assetType;
    private String url;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
}