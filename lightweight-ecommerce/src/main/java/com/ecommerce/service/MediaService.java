package com.ecommerce.service;

import com.ecommerce.config.MediaProperties;
import com.ecommerce.domain.AssetLink;
import com.ecommerce.domain.FileMetadata;
import com.ecommerce.domain.enums.AssetOwnerType;
import com.ecommerce.domain.enums.AssetPurpose;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.AttachAssetRequest;
import com.ecommerce.dto.FileUploadResponse;
import com.ecommerce.dto.StorageResult;
import com.ecommerce.repository.AssetLinkRepository;
import com.ecommerce.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final StorageService storageService;
    private final FileMetadataRepository fileMetadataRepository;
    private final AssetLinkRepository assetLinkRepository;
    private final MediaProperties mediaProperties;

    // ─── Upload only ─────────────────────────────────────────────────────────────

    @Transactional
    public FileUploadResponse upload(MultipartFile file, String category, String uploadedBy) {

        StorageResult result = storageService.store(file, category);

        FileMetadata metadata = FileMetadata.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(result.getStoredFileName())
                .storagePath(result.getStoragePath())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .checksum(result.getChecksum())
                .uploadedBy(uploadedBy)
                .build();

        FileMetadata saved = fileMetadataRepository.save(metadata);

        return toUploadResponse(saved);
    }

    // ─── Upload and attach to an owner ───────────────────────────────────────────

    @Transactional
    public AssetResponse uploadAndAttach(
            MultipartFile file,
            AssetOwnerType ownerType,
            Long ownerId,
            AssetPurpose purpose,
            String altText,
            Integer displayOrder,
            Boolean isPrimary,
            String uploadedBy
    ) {
        StorageResult result = storageService.store(file, ownerType.name().toLowerCase());

        FileMetadata metadata = FileMetadata.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(result.getStoredFileName())
                .storagePath(result.getStoragePath())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .checksum(result.getChecksum())
                .uploadedBy(uploadedBy)
                .build();

        FileMetadata savedFile = fileMetadataRepository.save(metadata);

        AssetLink link = AssetLink.builder()
                .file(savedFile)
                .ownerType(ownerType)
                .ownerId(ownerId)
                .purpose(purpose)
                .altText(altText)
                .displayOrder(displayOrder != null ? displayOrder : 0)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .build();

        AssetLink savedLink = assetLinkRepository.save(link);

        return toAssetResponse(savedLink);
    }

    // ─── Attach existing uploaded file ───────────────────────────────────────────

    @Transactional
    public AssetResponse attachExisting(AttachAssetRequest request) {

        FileMetadata file = fileMetadataRepository.findByPublicId(request.getPublicId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "File not found: " + request.getPublicId()
                ));

        AssetLink link = AssetLink.builder()
                .file(file)
                .ownerType(request.getOwnerType())
                .ownerId(request.getOwnerId())
                .purpose(request.getPurpose())
                .altText(request.getAltText())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
                .build();

        return toAssetResponse(assetLinkRepository.save(link));
    }

    // ─── Queries ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AssetResponse> getAssets(AssetOwnerType ownerType, Long ownerId) {
        return assetLinkRepository
                .findByOwnerTypeAndOwnerIdOrderByDisplayOrderAscIdAsc(ownerType, ownerId)
                .stream()
                .map(this::toAssetResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponse getPrimaryAsset(AssetOwnerType ownerType, Long ownerId) {
        return assetLinkRepository
                .findByOwnerTypeAndOwnerIdAndIsPrimaryTrue(ownerType, ownerId)
                .map(this::toAssetResponse)
                .orElse(null);
    }

    // ─── Delete ──────────────────────────────────────────────────────────────────

    @Transactional
    public void deleteAssetLink(Long assetLinkId) {
        AssetLink link = assetLinkRepository.findById(assetLinkId)
                .orElseThrow(() -> new IllegalArgumentException("Asset link not found: " + assetLinkId));

        assetLinkRepository.delete(link);

        // Only delete physical file if no other links use it
        boolean stillReferenced = assetLinkRepository.existsByFile_Id(link.getFile().getId());
        if (!stillReferenced) {
            storageService.delete(link.getFile().getStoragePath());
            fileMetadataRepository.delete(link.getFile());
        }
    }

    // ─── URL builder ─────────────────────────────────────────────────────────────

    public String buildUrl(String storagePath) {
        return mediaProperties.getBaseUrl() + "/" + storagePath;
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────────

    private FileUploadResponse toUploadResponse(FileMetadata file) {
        return FileUploadResponse.builder()
                .publicId(file.getPublicId())
                .originalFileName(file.getOriginalFileName())
                .contentType(file.getContentType())
                .fileSize(file.getFileSize())
                .url(buildUrl(file.getStoragePath()))
                .uploadedAt(file.getUploadedAt())
                .build();
    }

    private AssetResponse toAssetResponse(AssetLink link) {
        FileMetadata file = link.getFile();

        return AssetResponse.builder()
                .assetId(link.getId())
                .filePublicId(file.getPublicId())
                .originalFileName(file.getOriginalFileName())
                .contentType(file.getContentType())
                .fileSize(file.getFileSize())
                .ownerType(link.getOwnerType())
                .ownerId(link.getOwnerId())
                .purpose(link.getPurpose())
                .url(buildUrl(file.getStoragePath()))
                .altText(link.getAltText())
                .displayOrder(link.getDisplayOrder())
                .isPrimary(link.getIsPrimary())
                .build();
    }
}