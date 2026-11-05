package com.ecommerce.web;

import com.ecommerce.domain.enums.BannerSlot;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.BannerSlotInfo;
import com.ecommerce.dto.BannerSlotSnapshot;
import com.ecommerce.service.BannerSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banner-slots")
@RequiredArgsConstructor
public class BannerSlotController {

    private final BannerSlotService bannerSlotService;

    // ─── /snapshot MUST be first ──────────────────────────────
    @GetMapping("/snapshot")
    public ResponseEntity<Map<String, BannerSlotSnapshot>> getSnapshot() {
        return ResponseEntity.ok(bannerSlotService.getSnapshot());
    }

    // ─── Everything else after ────────────────────────────────
    @GetMapping
    public ResponseEntity<List<BannerSlotInfo>> getAllSlots() {
        return ResponseEntity.ok(bannerSlotService.getAllSlots());
    }

    @GetMapping("/{slot}")
    public ResponseEntity<BannerSlotInfo> getSlot(@PathVariable BannerSlot slot) {
        return ResponseEntity.ok(bannerSlotService.getSlot(slot));
    }

    @GetMapping("/{slot}/image")
    public ResponseEntity<AssetResponse> getCurrentImage(@PathVariable BannerSlot slot) {
        AssetResponse image = bannerSlotService.getCurrentImage(slot);
        return image != null
                ? ResponseEntity.ok(image)
                : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{slot}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssetResponse> upload(
            @PathVariable BannerSlot slot,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText",    required = false) String altText,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bannerSlotService.upload(slot, file, altText, uploadedBy));
    }

    @DeleteMapping("/{slot}")
    public ResponseEntity<Void> delete(@PathVariable BannerSlot slot) {
        bannerSlotService.delete(slot);
        return ResponseEntity.noContent().build();
    }
}