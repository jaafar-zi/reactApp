package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerSlotInfo {
    private String slot;
    private String ownerType;
    private Long   ownerId;
    private String description;
    private String currentImageUrl;   
    private Long   assetId;           
    private Boolean hasImage;
}