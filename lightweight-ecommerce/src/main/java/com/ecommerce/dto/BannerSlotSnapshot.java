package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerSlotSnapshot {
    private String  ownerType;
    private Long    ownerId;
    private String  url;        // null if no image uploaded yet
}