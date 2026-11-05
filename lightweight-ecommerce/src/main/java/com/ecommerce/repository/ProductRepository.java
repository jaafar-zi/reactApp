package com.ecommerce.repository;

import com.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.attributes
        LEFT JOIN FETCH p.pricing
        WHERE p.active = true
        ORDER BY p.createdAt DESC
    """)
    List<Product> findActiveProductsWithAttributes();

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.attributes
        LEFT JOIN FETCH p.pricing
        ORDER BY p.createdAt DESC
    """)
    List<Product> findAllWithAttributes();

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.attributes a
        LEFT JOIN FETCH a.attributeValues
        WHERE p IN :products
    """)
    List<Product> findProductsWithAttributeValues(
        @Param("products") List<Product> products
    );

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.assets
        WHERE p IN :products
    """)
    List<Product> findProductsWithAssets(
        @Param("products") List<Product> products
    );

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.variants
        WHERE p IN :products
    """)
    List<Product> findProductsWithVariants(
        @Param("products") List<Product> products
    );

    // ─── Single product ───────────────────────────────────────

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.attributes a
        LEFT JOIN FETCH a.attributeValues
        LEFT JOIN FETCH p.pricing
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithFullData(@Param("id") Long id);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.assets
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithAssets(@Param("id") Long id);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.variants
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithVariants(@Param("id") Long id);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.attributes a
        LEFT JOIN FETCH a.attributeValues
        LEFT JOIN FETCH p.pricing
        WHERE p.sku = :sku
    """)
    Optional<Product> findBySkuWithFullData(@Param("sku") String sku);

    boolean existsBySku(String sku);
}