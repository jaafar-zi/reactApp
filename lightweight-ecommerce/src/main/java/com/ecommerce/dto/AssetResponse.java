package com.ecommerce.dto;

import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetResponse {
    private Long assetId;
    private String filePublicId;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private AssetOwnerType ownerType;
    private Long ownerId;
    private AssetPurpose purpose;
    private String url;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
}