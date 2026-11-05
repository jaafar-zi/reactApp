package com.ecommerce.repository;

import com.ecommerce.domain.ProductAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAssetRepository extends JpaRepository<ProductAsset, Long> {

    List<ProductAsset> findByProductIdOrderByDisplayOrderAscIdAsc(Long productId);

    Optional<ProductAsset> findByIdAndProductId(Long id, Long productId);

    boolean existsByProductIdAndIsPrimaryTrue(Long productId);
}