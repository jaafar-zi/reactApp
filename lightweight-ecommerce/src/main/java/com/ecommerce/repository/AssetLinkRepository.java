package com.ecommerce.repository;

import com.ecommerce.domain.AssetLink;
import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetLinkRepository extends JpaRepository<AssetLink, Long> {

    List<AssetLink> findByOwnerTypeAndOwnerIdOrderByDisplayOrderAscIdAsc(
            AssetOwnerType ownerType,
            Long ownerId
    );

    List<AssetLink> findByOwnerTypeAndOwnerIdAndPurposeOrderByDisplayOrderAscIdAsc(
            AssetOwnerType ownerType,
            Long ownerId,
            AssetPurpose purpose
    );

    Optional<AssetLink> findByOwnerTypeAndOwnerIdAndIsPrimaryTrue(
            AssetOwnerType ownerType,
            Long ownerId
    );

    boolean existsByFile_Id(Long fileId);
}