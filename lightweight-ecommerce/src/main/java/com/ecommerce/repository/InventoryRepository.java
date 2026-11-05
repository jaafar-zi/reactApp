package com.ecommerce.repository;

import com.ecommerce.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findByVariantId(Long variantId);

    List<Inventory> findByProductIdOrVariant_ProductId(Long productId, Long variantProductId);
}