package com.ecommerce.domain;

import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "asset_links",
    indexes = {
        @Index(name = "idx_asset_owner", columnList = "ownerType, ownerId"),
        @Index(name = "idx_asset_file",  columnList = "file_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AssetOwnerType ownerType;

    @Column(nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AssetPurpose purpose;

    private String altText;

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;
}