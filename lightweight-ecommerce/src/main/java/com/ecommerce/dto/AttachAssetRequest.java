package com.ecommerce.dto;

import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachAssetRequest {

    private String       publicId;
    private AssetOwnerType ownerType;
    private Long         ownerId;
    private AssetPurpose purpose;
    private String       altText;
    private Integer      displayOrder;
    private Boolean      isPrimary;
}