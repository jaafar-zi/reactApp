package com.ecommerce.web;

import com.ecommerce.dto.CreateProductRequest;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.UpdateProductRequest;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ─── GET all ──────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "en") String language
    ) {
        List<ProductDTO> products = activeOnly
                ? productService.getActiveProducts(language)
                : productService.getAllProducts(language);

        return ResponseEntity.ok(products);
    }

    // ─── GET by ID ────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(productService.getProductById(id, language));
    }

    // ─── GET by SKU ───────────────────────────────────────────

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(
            @PathVariable String sku,
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(productService.getProductBySku(sku, language));
    }

    // ─── POST create ──────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, language));
    }

    // ─── PUT update ───────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request,
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request, language));
    }

    // ─── DELETE ───────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ─── PATCH toggle active ──────────────────────────────────

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ProductDTO> toggleActive(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(productService.toggleActive(id, language));
    }
}