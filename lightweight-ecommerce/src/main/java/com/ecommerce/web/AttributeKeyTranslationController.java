package com.ecommerce.web;

import com.ecommerce.dto.AttributeKeyTranslationDTO;
import com.ecommerce.service.AttributeKeyTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attribute-key-translations")
@RequiredArgsConstructor
public class AttributeKeyTranslationController {

    private final AttributeKeyTranslationService service;

    /**
     * GET /api/attribute-key-translations?language=en
     * Returns list of { attributeKey, label }
     */
    @GetMapping
    public ResponseEntity<List<AttributeKeyTranslationDTO>> getTranslations(
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(service.getTranslations(language));
    }

    /**
     * GET /api/attribute-key-translations/map?language=en
     * Returns { "product_details": "Product Details", ... }
     */
    @GetMapping("/map")
    public ResponseEntity<Map<String, String>> getTranslationMap(
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(service.getTranslationMap(language));
    }
}