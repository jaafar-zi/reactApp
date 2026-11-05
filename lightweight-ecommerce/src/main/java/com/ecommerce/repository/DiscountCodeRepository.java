package com.ecommerce.repository;

import com.ecommerce.domain.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long> {
    
    Optional<DiscountCode> findByUserPrefixAndActiveTrue(String userPrefix);

    Optional<DiscountCode> findByCodeIgnoreCase(String code);
    
    boolean existsByCodeIgnoreCase(String code);
}