package com.ecommerce.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.config.StorageProperties;

import java.net.MalformedURLException;
import java.nio.file.*;

@Slf4j
@Profile("dev")
@RestController
@RequiredArgsConstructor
public class DevMediaFallbackController {

    private final StorageProperties storageProperties;

    /**
     * Simulates Nginx static file serving in local dev.
     * In production, Nginx handles /media/** before reaching Spring Boot.
     * This controller is NEVER active in production.
     */
    @GetMapping("/media/**")
    public ResponseEntity<Resource> serveMedia(HttpServletRequest request) {

        String requestPath = request.getRequestURI();
        String relativePath = requestPath.replaceFirst("^/media/", "");

        Path basePath = Paths.get(storageProperties.getBaseDir()).toAbsolutePath().normalize();
        Path filePath = basePath.resolve(relativePath).normalize();

        // Security: prevent path traversal
        if (!filePath.startsWith(basePath)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Media file not found: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error serving media file: {}", relativePath, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}