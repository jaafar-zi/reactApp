package com.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.config.StorageProperties;
import com.ecommerce.dto.StorageResult;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageProperties storageProperties;

    private Path baseStoragePath;

    @PostConstruct
    public void init() {
        baseStoragePath = Paths.get(storageProperties.getBaseDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(baseStoragePath);
            log.info("Storage initialized at: {}", baseStoragePath);
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize storage at: " + baseStoragePath, e);
        }
    }

    /**
     * Stores the file under a category subfolder.
     *
     * @param file     the uploaded file
     * @param category folder name: products, categories, brands, etc.
     * @return StorageResult with storedFileName, storagePath, checksum
     */
    public StorageResult store(MultipartFile file, String category) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown"
        );

        String extension = extractExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);
        String storagePath = category + "/" + storedFileName;

        Path categoryDir = baseStoragePath.resolve(category).normalize();
        Path targetFile = categoryDir.resolve(storedFileName).normalize();

        // Security: prevent path traversal
        if (!targetFile.startsWith(baseStoragePath)) {
            throw new IllegalArgumentException("Invalid file path detected");
        }

        try {
            Files.createDirectories(categoryDir);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file: " + originalFileName, e);
        }

        String checksum = computeChecksum(targetFile);

        log.info("File stored: {}", storagePath);

        return StorageResult.builder()
                .storedFileName(storedFileName)
                .storagePath(storagePath)
                .checksum(checksum)
                .build();
    }

    /**
     * Deletes a file from disk using its relative storage path.
     */
    public void delete(String storagePath) {
        Path target = baseStoragePath.resolve(storagePath).normalize();

        if (!target.startsWith(baseStoragePath)) {
            throw new IllegalArgumentException("Invalid path: " + storagePath);
        }

        try {
            Files.deleteIfExists(target);
            log.info("File deleted: {}", storagePath);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", storagePath, e);
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────────

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !storageProperties.getAllowedTypes().contains(contentType)) {
            throw new IllegalArgumentException(
                    "File type not allowed: " + contentType +
                    ". Allowed: " + storageProperties.getAllowedTypes()
            );
        }

        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new IllegalArgumentException(
                    "File too large. Max allowed: " + storageProperties.getMaxFileSize() + " bytes"
            );
        }
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex >= 0) ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }

    private String computeChecksum(Path filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = Files.readAllBytes(filePath);
            byte[] hash = digest.digest(bytes);
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            log.warn("Could not compute checksum for: {}", filePath);
            return null;
        }
    }
}