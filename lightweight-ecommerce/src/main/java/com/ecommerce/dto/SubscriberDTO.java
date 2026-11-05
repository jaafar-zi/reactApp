package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberDTO {
    
    private Long id;
    
    @NotBlank
    private String name;
    
    @NotBlank
    @Email
    private String email;
    
    private Boolean active;
}