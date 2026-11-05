package com.ecommerce.service;

import com.ecommerce.domain.Inventory;
import com.ecommerce.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private Inventory findInventory(Long productId, Long variantId) {
        if (variantId != null) {
            return inventoryRepository.findByVariantId(variantId)
                    .orElseThrow(() -> new RuntimeException(
                            "Inventory not found for variant: " + variantId));
        }
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException(
                        "Inventory not found for product: " + productId));
    }

    @Transactional
    public boolean reserveStock(Long productId, Long variantId, int quantity) {
        Inventory inventory = findInventory(productId, variantId);

        if (inventory.getAvailableQuantity() < quantity) {
            log.warn("Insufficient stock - productId: {}, variantId: {}, requested: {}, available: {}",
                    productId, variantId, quantity, inventory.getAvailableQuantity());
            return false;
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        log.info("Reserved {} units - productId: {}, variantId: {}", quantity, productId, variantId);
        return true;
    }

    @Transactional
    public void confirmReservation(Long productId, Long variantId, int quantity) {
        Inventory inventory = findInventory(productId, variantId);
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(
                Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
        log.info("Confirmed {} units - productId: {}, variantId: {}", quantity, productId, variantId);
    }

    @Transactional
    public void releaseReservation(Long productId, Long variantId, int quantity) {
        Inventory inventory = findInventory(productId, variantId);
        inventory.setReservedQuantity(
                Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
        log.info("Released {} units - productId: {}, variantId: {}", quantity, productId, variantId);
    }
}