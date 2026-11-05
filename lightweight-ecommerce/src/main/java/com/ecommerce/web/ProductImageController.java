package com.ecommerce.web;

import com.ecommerce.dto.ProductAssetDTO;
import com.ecommerce.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    // ─── Upload new image ─────────────────────────────────────

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductAssetDTO> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText",      required = false) String altText,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isPrimary",    defaultValue = "false") Boolean isPrimary,
            @RequestParam(value = "uploadedBy",   required = false) String uploadedBy
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productImageService.uploadImage(
                        productId, file, altText, displayOrder, isPrimary, uploadedBy
                ));
    }

    // ─── Attach already-uploaded file ─────────────────────────

    @PostMapping("/attach")
    public ResponseEntity<ProductAssetDTO> attachExisting(
            @PathVariable Long productId,
            @RequestParam("filePublicId") String filePublicId,
            @RequestParam(value = "altText",      required = false) String altText,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isPrimary",    defaultValue = "false") Boolean isPrimary
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productImageService.attachExistingImage(
                        productId, filePublicId, altText, displayOrder, isPrimary
                ));
    }

    // ─── Get all images ───────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductAssetDTO>> getImages(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productImageService.getImages(productId));
    }

    // ─── Get primary image ────────────────────────────────────

    @GetMapping("/primary")
    public ResponseEntity<ProductAssetDTO> getPrimaryImage(
            @PathVariable Long productId
    ) {
        ProductAssetDTO primary = productImageService.getPrimaryImage(productId);
        return primary != null
                ? ResponseEntity.ok(primary)
                : ResponseEntity.notFound().build();
    }

    // ─── Set primary image ────────────────────────────────────

    @PatchMapping("/{assetId}/set-primary")
    public ResponseEntity<ProductAssetDTO> setPrimary(
            @PathVariable Long productId,
            @PathVariable Long assetId
    ) {
        return ResponseEntity.ok(productImageService.setPrimaryImage(productId, assetId));
    }

    // ─── Update image metadata ────────────────────────────────

    @PatchMapping("/{assetId}")
    public ResponseEntity<ProductAssetDTO> updateMetadata(
            @PathVariable Long productId,
            @PathVariable Long assetId,
            @RequestParam(value = "altText",      required = false) String altText,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isPrimary",    required = false) Boolean isPrimary
    ) {
        return ResponseEntity.ok(productImageService.updateImageMetadata(
                productId, assetId, altText, displayOrder, isPrimary
        ));
    }

    // ─── Delete image ─────────────────────────────────────────

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable Long assetId
    ) {
        productImageService.deleteImage(productId, assetId);
        return ResponseEntity.noContent().build();
    }
}