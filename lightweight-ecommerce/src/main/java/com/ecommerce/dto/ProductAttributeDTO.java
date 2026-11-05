package com.ecommerce.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeDTO {

    private Long id;
    private String attributeKey;
    private String attributeKeyLabel;  
    private String language;

    @Builder.Default
    private List<AttributeValueDTO> attributeValues = new ArrayList<>();
}