package com.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeKeyTranslationDTO {
    private String attributeKey;
    private String label;
}