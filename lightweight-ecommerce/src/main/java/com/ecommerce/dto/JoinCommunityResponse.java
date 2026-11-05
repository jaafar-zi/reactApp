package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinCommunityResponse {

    private Long subscriberId;
    private String name;
    private String email;
    private Boolean isNewSubscriber;
    private String discountCode;
    private String discountDescription;
}