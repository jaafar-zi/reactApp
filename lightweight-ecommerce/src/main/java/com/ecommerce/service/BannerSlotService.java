package com.ecommerce.service;

import com.ecommerce.domain.enums.AssetPurpose;
import com.ecommerce.domain.enums.BannerSlot;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.BannerSlotInfo;
import com.ecommerce.dto.BannerSlotSnapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerSlotService {

    private final MediaService mediaService;

    public Map<String, BannerSlotSnapshot> getSnapshot() {
        return Arrays.stream(BannerSlot.values())
                .collect(Collectors.toMap(
                        BannerSlot::name,
                        slot -> {
                            AssetResponse current = mediaService.getPrimaryAsset(
                                    slot.getAssetOwnerType(),
                                    slot.getOwnerId()
                            );
                            return BannerSlotSnapshot.builder()
                                    .ownerType(slot.getOwnerType())
                                    .ownerId(slot.getOwnerId())
                                    .url(current != null ? current.getUrl() : null)
                                    .build();
                        }
                ));
    }

    // ─── List all slots ───────────────────────────────────────

    public List<BannerSlotInfo> getAllSlots() {
        return Arrays.stream(BannerSlot.values())
                .map(this::toSlotInfo)
                .toList();
    }

    // ─── Get single slot info ─────────────────────────────────

    public BannerSlotInfo getSlot(BannerSlot slot) {
        return toSlotInfo(slot);
    }

    // ─── Upload image to slot ─────────────────────────────────

    public AssetResponse upload(
            BannerSlot slot,
            MultipartFile file,
            String altText,
            String uploadedBy
    ) {
        // Delete existing image first (replace semantics)
        deleteExistingIfPresent(slot);

        AssetResponse response = mediaService.uploadAndAttach(
                file,
                slot.getAssetOwnerType(),
                slot.getOwnerId(),
                AssetPurpose.GALLERY,
                altText,
                0,
                true,
                uploadedBy
        );

        log.info("Banner slot [{}] image uploaded: url={}", slot.name(), response.getUrl());

        return response;
    }

    // ─── Delete image from slot ───────────────────────────────

    public void delete(BannerSlot slot) {
        deleteExistingIfPresent(slot);
        log.info("Banner slot [{}] image deleted", slot.name());
    }

    // ─── Get current image for slot ───────────────────────────

    public AssetResponse getCurrentImage(BannerSlot slot) {
        return mediaService.getPrimaryAsset(
                slot.getAssetOwnerType(),
                slot.getOwnerId()
        );
    }

    // ─── Private helpers ──────────────────────────────────────

    private void deleteExistingIfPresent(BannerSlot slot) {
        AssetResponse existing = mediaService.getPrimaryAsset(
                slot.getAssetOwnerType(),
                slot.getOwnerId()
        );
        if (existing != null) {
            mediaService.deleteAssetLink(existing.getAssetId());
            log.info("Banner slot [{}] old image removed: assetId={}",
                    slot.name(), existing.getAssetId());
        }
    }

    private BannerSlotInfo toSlotInfo(BannerSlot slot) {
        AssetResponse current = mediaService.getPrimaryAsset(
                slot.getAssetOwnerType(),
                slot.getOwnerId()
        );

        return BannerSlotInfo.builder()
                .slot(slot.name())
                .ownerType(slot.getOwnerType())
                .ownerId(slot.getOwnerId())
                .description(slot.getDescription())
                .currentImageUrl(current != null ? current.getUrl() : null)
                .assetId(current != null ? current.getAssetId() : null)
                .hasImage(current != null)
                .build();
    }
}