package com.ecommerce.web;

import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.AttachAssetRequest;
import com.ecommerce.dto.FileUploadResponse;
import com.ecommerce.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // ─── Upload only ─────────────────────────────────────────────────────────────

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", defaultValue = "misc") String category,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mediaService.upload(file, category, uploadedBy));
    }

    // ─── Upload and attach ───────────────────────────────────────────────────────

    @PostMapping(value = "/upload-and-attach", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssetResponse> uploadAndAttach(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ownerType") AssetOwnerType ownerType,
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("purpose") AssetPurpose purpose,
            @RequestParam(value = "altText",      required = false) String altText,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isPrimary",    required = false) Boolean isPrimary,
            @RequestParam(value = "uploadedBy",   required = false) String uploadedBy
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mediaService.uploadAndAttach(
                        file, ownerType, ownerId, purpose,
                        altText, displayOrder, isPrimary, uploadedBy
                ));
    }

    // ─── Attach existing ─────────────────────────────────────────────────────────

    @PostMapping("/attach")
    public ResponseEntity<AssetResponse> attach(@RequestBody AttachAssetRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mediaService.attachExisting(request));
    }

    // ─── Queries ─────────────────────────────────────────────────────────────────

    @GetMapping("/owners/{ownerType}/{ownerId}")
    public ResponseEntity<List<AssetResponse>> getAssets(
            @PathVariable AssetOwnerType ownerType,
            @PathVariable Long ownerId
    ) {
        return ResponseEntity.ok(mediaService.getAssets(ownerType, ownerId));
    }

    @GetMapping("/owners/{ownerType}/{ownerId}/primary")
    public ResponseEntity<AssetResponse> getPrimary(
            @PathVariable AssetOwnerType ownerType,
            @PathVariable Long ownerId
    ) {
        AssetResponse primary = mediaService.getPrimaryAsset(ownerType, ownerId);
        return primary != null
                ? ResponseEntity.ok(primary)
                : ResponseEntity.notFound().build();
    }

    // ─── Delete ──────────────────────────────────────────────────────────────────

    @DeleteMapping("/{assetLinkId}")
    public ResponseEntity<Void> delete(@PathVariable Long assetLinkId) {
        mediaService.deleteAssetLink(assetLinkId);
        return ResponseEntity.noContent().build();
    }
}