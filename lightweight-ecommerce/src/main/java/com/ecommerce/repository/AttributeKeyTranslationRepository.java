package com.ecommerce.repository;

import com.ecommerce.domain.AttributeKeyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeKeyTranslationRepository extends JpaRepository<AttributeKeyTranslation, Long> {

    List<AttributeKeyTranslation> findByLanguage(String language);

    Optional<AttributeKeyTranslation> findByAttributeKeyAndLanguage(String attributeKey, String language);
}