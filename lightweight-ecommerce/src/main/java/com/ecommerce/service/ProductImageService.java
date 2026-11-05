package com.ecommerce.service;

import com.ecommerce.domain.Product;
import com.ecommerce.domain.ProductAsset;
import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.AttachAssetRequest;
import com.ecommerce.dto.ProductAssetDTO;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.repository.ProductAssetRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository       productRepository;
    private final ProductAssetRepository  productAssetRepository;
    private final MediaService            mediaService;

    // ─── Upload and attach image to product ───────────────────

    @Transactional
    public ProductAssetDTO uploadImage(
            Long productId,
            MultipartFile file,
            String altText,
            Integer displayOrder,
            Boolean isPrimary,
            String uploadedBy
    ) {
        Product product = findProductOrThrow(productId);

        // If this is marked primary, demote any existing primary first
        if (Boolean.TRUE.equals(isPrimary)) {
            demoteExistingPrimary(productId);
        }

        // Resolve display order if not provided
        int resolvedOrder = resolveDisplayOrder(productId, displayOrder);

        // Delegate upload + AssetLink creation to MediaService
        AssetResponse assetResponse = mediaService.uploadAndAttach(
                file,
                AssetOwnerType.PRODUCT,
                productId,
                AssetPurpose.GALLERY,
                altText,
                resolvedOrder,
                isPrimary,
                uploadedBy
        );

        // Mirror into ProductAsset for direct product queries
        ProductAsset asset = ProductAsset.builder()
                .product(product)
                .assetType(file.getContentType())
                .url(assetResponse.getUrl())
                .altText(altText)
                .displayOrder(resolvedOrder)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .assetLinkId(assetResponse.getAssetId())
                .build();

        ProductAsset saved = productAssetRepository.save(asset);
        product.addAsset(saved);

        log.info("Image uploaded for product {}: assetLinkId={}", productId, assetResponse.getAssetId());

        return toDTO(saved);
    }

    // ─── Attach already-uploaded file to product ──────────────

    @Transactional
    public ProductAssetDTO attachExistingImage(
            Long productId,
            String filePublicId,
            String altText,
            Integer displayOrder,
            Boolean isPrimary
    ) {
        Product product = findProductOrThrow(productId);

        if (Boolean.TRUE.equals(isPrimary)) {
            demoteExistingPrimary(productId);
        }

        int resolvedOrder = resolveDisplayOrder(productId, displayOrder);

        // Use MediaService to create the AssetLink for the existing file
        com.ecommerce.dto.AttachAssetRequest attachRequest =
                AttachAssetRequest.builder()
                        .publicId(filePublicId)
                        .ownerType(AssetOwnerType.PRODUCT)
                        .ownerId(productId)
                        .purpose(AssetPurpose.GALLERY)
                        .altText(altText)
                        .displayOrder(resolvedOrder)
                        .isPrimary(isPrimary)
                        .build();

        AssetResponse assetResponse = mediaService.attachExisting(attachRequest);

        ProductAsset asset = ProductAsset.builder()
                .product(product)
                .assetType(assetResponse.getContentType())
                .url(assetResponse.getUrl())
                .altText(altText)
                .displayOrder(resolvedOrder)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .assetLinkId(assetResponse.getAssetId())
                .build();

        ProductAsset saved = productAssetRepository.save(asset);
        product.addAsset(saved);

        log.info("Existing image attached to product {}: assetLinkId={}", productId, assetResponse.getAssetId());

        return toDTO(saved);
    }

    // ─── Get all images for a product ────────────────────────

    @Transactional(readOnly = true)
    public List<ProductAssetDTO> getImages(Long productId) {
        findProductOrThrow(productId);
        return productAssetRepository
                .findByProductIdOrderByDisplayOrderAscIdAsc(productId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ─── Get primary image ────────────────────────────────────

    @Transactional(readOnly = true)
    public ProductAssetDTO getPrimaryImage(Long productId) {
        findProductOrThrow(productId);
        return mediaService.getPrimaryAsset(AssetOwnerType.PRODUCT, productId) != null
                ? toDTO(productAssetRepository
                    .findByProductIdOrderByDisplayOrderAscIdAsc(productId)
                    .stream()
                    .filter(a -> Boolean.TRUE.equals(a.getIsPrimary()))
                    .findFirst()
                    .orElse(null))
                : null;
    }

    // ─── Set primary image ────────────────────────────────────

    @Transactional
    public ProductAssetDTO setPrimaryImage(Long productId, Long assetId) {
        findProductOrThrow(productId);

        // Demote current primary
        demoteExistingPrimary(productId);

        // Promote new primary
        ProductAsset asset = productAssetRepository
                .findByIdAndProductId(assetId, productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Asset not found: " + assetId + " for product: " + productId
                ));

        asset.setIsPrimary(true);
        ProductAsset saved = productAssetRepository.save(asset);

        log.info("Primary image set for product {}: assetId={}", productId, assetId);

        return toDTO(saved);
    }

    // ─── Update image metadata ────────────────────────────────

    @Transactional
    public ProductAssetDTO updateImageMetadata(
            Long productId,
            Long assetId,
            String altText,
            Integer displayOrder,
            Boolean isPrimary
    ) {
        findProductOrThrow(productId);

        ProductAsset asset = productAssetRepository
                .findByIdAndProductId(assetId, productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Asset not found: " + assetId + " for product: " + productId
                ));

        if (Boolean.TRUE.equals(isPrimary) && !Boolean.TRUE.equals(asset.getIsPrimary())) {
            demoteExistingPrimary(productId);
        }

        if (altText != null)     asset.setAltText(altText);
        if (displayOrder != null) asset.setDisplayOrder(displayOrder);
        if (isPrimary != null)    asset.setIsPrimary(isPrimary);

        return toDTO(productAssetRepository.save(asset));
    }

    // ─── Delete image ─────────────────────────────────────────

    @Transactional
    public void deleteImage(Long productId, Long assetId) {
        findProductOrThrow(productId);

        ProductAsset asset = productAssetRepository
                .findByIdAndProductId(assetId, productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Asset not found: " + assetId + " for product: " + productId
                ));

        // Delegate physical file + AssetLink deletion to MediaService
        if (asset.getAssetLinkId() != null) {
            mediaService.deleteAssetLink(asset.getAssetLinkId());
        }

        productAssetRepository.delete(asset);

        log.info("Image deleted for product {}: assetId={}", productId, assetId);
    }

    // ─── Private helpers ──────────────────────────────────────

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + productId
                ));
    }

    /**
     * Demotes the current primary asset for the product, if any.
     */
    private void demoteExistingPrimary(Long productId) {
        productAssetRepository
                .findByProductIdOrderByDisplayOrderAscIdAsc(productId)
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsPrimary()))
                .findFirst()
                .ifPresent(a -> {
                    a.setIsPrimary(false);
                    productAssetRepository.save(a);
                });
    }

    /**
     * Auto-assigns display order as last + 1 if not provided.
     */
    private int resolveDisplayOrder(Long productId, Integer displayOrder) {
        if (displayOrder != null) return displayOrder;

        List<ProductAsset> existing =
                productAssetRepository.findByProductIdOrderByDisplayOrderAscIdAsc(productId);

        return existing.isEmpty()
                ? 0
                : existing.get(existing.size() - 1).getDisplayOrder() + 1;
    }

    // ─── Mapper ───────────────────────────────────────────────

    private ProductAssetDTO toDTO(ProductAsset asset) {
        if (asset == null) return null;
        return ProductAssetDTO.builder()
                .id(asset.getId())
                .assetType(asset.getAssetType())
                .url(asset.getUrl())
                .altText(asset.getAltText())
                .displayOrder(asset.getDisplayOrder())
                .isPrimary(asset.getIsPrimary())
                .build();
    }
}