package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "attribute_key_translations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"attribute_key", "language"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeKeyTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_key", nullable = false)
    private String attributeKey;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false)
    private String label;
}