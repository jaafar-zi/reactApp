// src/main/java/com/yourcompany/ecommerce/controller/ShippingController.java
package com.ecommerce.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dto.CreateShippingRequest;
import com.ecommerce.dto.ShippingDTO;
import com.ecommerce.dto.ShippingStatusUpdateRequest;
import com.ecommerce.service.ShippingService;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShippingController {
    
    private final ShippingService shippingService;
    
    @PostMapping
    public ResponseEntity<ShippingDTO> createShipping(@Valid @RequestBody CreateShippingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shippingService.createShipping(request));
    }
    
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ShippingDTO> trackShipping(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shippingService.getShippingByTrackingNumber(trackingNumber));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShippingDTO> getShippingByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.getShippingByOrderId(orderId));
    }
    
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<ShippingDTO>> getCustomerShippings(@PathVariable String email) {
        return ResponseEntity.ok(shippingService.getShippingsByCustomer(email));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShippingDTO>> getShippingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(shippingService.getShippingsByStatus(status));
    }
    
    @PatchMapping("/track/{trackingNumber}/status")
    public ResponseEntity<ShippingDTO> updateShippingStatus(
            @PathVariable String trackingNumber,
            @Valid @RequestBody ShippingStatusUpdateRequest request) {
        return ResponseEntity.ok(shippingService.updateShippingStatus(trackingNumber, request));
    }
    
    @PutMapping("/track/{trackingNumber}")
    public ResponseEntity<ShippingDTO> updateShipping(
            @PathVariable String trackingNumber,
            @Valid @RequestBody ShippingDTO shippingDTO) {
        return ResponseEntity.ok(shippingService.updateShipping(trackingNumber, shippingDTO));
    }
}