package com.ecommerce.repository;

import com.ecommerce.domain.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByPublicId(String publicId);

    Optional<FileMetadata> findByStoredFileName(String storedFileName);

    List<FileMetadata> findByUploadedBy(String uploadedBy);
}