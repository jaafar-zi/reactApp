package com.ecommerce.service;

import com.ecommerce.domain.Inventory;
import com.ecommerce.domain.Product;
import com.ecommerce.domain.ProductVariant;
import com.ecommerce.dto.CreateProductRequest;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.ProductVariantDTO;
import com.ecommerce.dto.UpdateProductRequest;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repository.InventoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryRepository inventoryRepository;

    // ─── Active products ──────────────────────────────────────

    public List<ProductDTO> getActiveProducts(String language) {
        List<Product> products = productRepository.findActiveProductsWithAttributes();
        if (products.isEmpty()) return Collections.emptyList();

        productRepository.findProductsWithAttributeValues(products);
        productRepository.findProductsWithAssets(products);
        productRepository.findProductsWithVariants(products);

        return products.stream()
                .map(p -> enrichWithInventory(productMapper.toDTO(p, language), p))
                .toList();
    }

    // ─── All products (admin) ─────────────────────────────────

    public List<ProductDTO> getAllProducts(String language) {
        List<Product> products = productRepository.findAllWithAttributes();
        if (products.isEmpty()) return Collections.emptyList();

        productRepository.findProductsWithAttributeValues(products);
        productRepository.findProductsWithAssets(products);
        productRepository.findProductsWithVariants(products);

        return products.stream()
                .map(p -> enrichWithInventory(productMapper.toDTO(p, language), p))
                .toList();
    }

    // ─── Single product by ID ─────────────────────────────────

    public ProductDTO getProductById(Long id, String language) {
        Product product = productRepository.findByIdWithFullData(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        productRepository.findByIdWithAssets(id);
        productRepository.findByIdWithVariants(id);

        return enrichWithInventory(productMapper.toDTO(product, language), product);
    }

    // ─── Single product by SKU ────────────────────────────────

    public ProductDTO getProductBySku(String sku, String language) {
        Product product = productRepository.findBySkuWithFullData(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with sku: " + sku));

        return enrichWithInventory(productMapper.toDTO(product, language), product);
    }

    // ─── Create ───────────────────────────────────────────────

    @Transactional
    public ProductDTO createProduct(CreateProductRequest request, String language) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product already exists with SKU: " + request.getSku());
        }

        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);

        if (saved.hasVariants()) {
            // Create inventory per variant
            for (int i = 0; i < saved.getVariants().size(); i++) {
                ProductVariant variant = saved.getVariants().get(i);
                int initialStock = 0;
                if (request.getVariants() != null
                        && i < request.getVariants().size()
                        && request.getVariants().get(i).getInitialStock() != null) {
                    initialStock = request.getVariants().get(i).getInitialStock();
                }

                Inventory inventory = Inventory.builder()
                        .variant(variant)
                        .quantity(initialStock)
                        .reservedQuantity(0)
                        .minStockLevel(5)
                        .build();
                inventoryRepository.save(inventory);
            }
        } else {
            // Create single product-level inventory
            Inventory inventory = Inventory.builder()
                    .product(saved)
                    .quantity(0)
                    .reservedQuantity(0)
                    .minStockLevel(5)
                    .build();
            inventoryRepository.save(inventory);
        }

        return enrichWithInventory(productMapper.toDTO(saved, language), saved);
    }

    // ─── Update ───────────────────────────────────────────────

    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductRequest request, String language) {
        Product existing = productRepository.findByIdWithFullData(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        productMapper.updateEntityFromRequest(request, existing);
        Product updated = productRepository.save(existing);

        return enrichWithInventory(productMapper.toDTO(updated, language), updated);
    }

    // ─── Delete ───────────────────────────────────────────────

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // ─── Toggle active ────────────────────────────────────────

    @Transactional
    public ProductDTO toggleActive(Long id, String language) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        product.setActive(!product.getActive());
        Product saved = productRepository.save(product);

        return enrichWithInventory(productMapper.toDTO(saved, language), saved);
    }

    // ─── Inventory enrichment ─────────────────────────────────

    private ProductDTO enrichWithInventory(ProductDTO dto, Product product) {
        if (product.hasVariants()) {
            // Enrich each variant with its inventory
            if (dto.getVariants() != null) {
                for (ProductVariantDTO variantDTO : dto.getVariants()) {
                    inventoryRepository.findByVariantId(variantDTO.getId()).ifPresent(inv ->
                        variantDTO.setInventory(ProductVariantDTO.VariantInventoryDTO.builder()
                                .quantity(inv.getQuantity())
                                .availableQuantity(inv.getAvailableQuantity())
                                .inStock(inv.isInStock())
                                .lowStock(inv.isLowStock())
                                .build())
                    );
                }
            }

            // Aggregate inventory at product level
            int totalQty = dto.getVariants().stream()
                    .filter(v -> v.getInventory() != null)
                    .mapToInt(v -> v.getInventory().getQuantity())
                    .sum();
            int totalAvail = dto.getVariants().stream()
                    .filter(v -> v.getInventory() != null)
                    .mapToInt(v -> v.getInventory().getAvailableQuantity())
                    .sum();

            dto.setInventory(ProductDTO.InventoryDTO.builder()
                    .quantity(totalQty)
                    .availableQuantity(totalAvail)
                    .inStock(totalAvail > 0)
                    .lowStock(totalAvail <= 5)
                    .build());
        } else {
            // Simple product-level inventory
            inventoryRepository.findByProductId(product.getId()).ifPresent(inv ->
                dto.setInventory(ProductDTO.InventoryDTO.builder()
                        .quantity(inv.getQuantity())
                        .availableQuantity(inv.getAvailableQuantity())
                        .inStock(inv.isInStock())
                        .lowStock(inv.isLowStock())
                        .build())
            );
        }
        return dto;
    }
}