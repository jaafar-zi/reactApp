package com.ecommerce.web;

import com.ecommerce.domain.DiscountCode;
import com.ecommerce.domain.Order;
import com.ecommerce.dto.ApplyDiscountCodeRequest;
import com.ecommerce.dto.CreateDiscountCodeRequest;
import com.ecommerce.service.DiscountCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiscountCodeController {

    private final DiscountCodeService discountCodeService;

    @PostMapping("/admin/discount-codes")
    public ResponseEntity<DiscountCode> createDiscountCode(
            @Valid @RequestBody CreateDiscountCodeRequest request) {
        return ResponseEntity.ok(discountCodeService.createDiscountCode(request));
    }

    @PostMapping("/orders/{orderId}/apply-discount")
    public ResponseEntity<Order> applyDiscountCode(
            @PathVariable Long orderId,
            @Valid @RequestBody ApplyDiscountCodeRequest request) {
        return ResponseEntity.ok(
            discountCodeService.applyDiscountCode(orderId, request.getCode())
        );
    }

    @GetMapping("/discount-codes/validate")
    public ResponseEntity<DiscountCode> validateDiscountCode(
            @RequestParam String code) {
        return ResponseEntity.ok(discountCodeService.validateCode(code));
    }
}