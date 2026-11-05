// src/main/java/com/yourcompany/ecommerce/controller/OrderController.java
package com.ecommerce.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.domain.OrderStatus;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderDTO));
    }
    
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByNumber(orderNumber));
    }
    
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerEmail(email));
    }
    
    @PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderNumber,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderNumber, status));
    }
}