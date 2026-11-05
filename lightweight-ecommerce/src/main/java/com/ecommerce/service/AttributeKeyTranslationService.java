package com.ecommerce.service;

import com.ecommerce.domain.AttributeKeyTranslation;
import com.ecommerce.dto.AttributeKeyTranslationDTO;
import com.ecommerce.repository.AttributeKeyTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeKeyTranslationService {

    private final AttributeKeyTranslationRepository repository;

    /**
     * Returns all attribute key translations for a given language.
     */
    public List<AttributeKeyTranslationDTO> getTranslations(String language) {
        return repository.findByLanguage(language).stream()
                .map(t -> AttributeKeyTranslationDTO.builder()
                        .attributeKey(t.getAttributeKey())
                        .label(t.getLabel())
                        .build())
                .toList();
    }

    /**
     * Returns a map of attributeKey → label for a given language.
     */
    public Map<String, String> getTranslationMap(String language) {
        return repository.findByLanguage(language).stream()
                .collect(Collectors.toMap(
                        AttributeKeyTranslation::getAttributeKey,
                        AttributeKeyTranslation::getLabel,
                        (a, b) -> a
                ));
    }
}