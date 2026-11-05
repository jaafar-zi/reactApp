package com.ecommerce.service;

import com.ecommerce.domain.*;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.mapper.OrderEmailMapper;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryService inventoryService;
    private final OrderEmailMapper emailMapper;
    private final MailService mailService;

    public OrderDTO createOrder(OrderDTO dto) {

        // ── 1. Basic validation ──────────────────────────────────────
        if (dto.getCustomerPhone() == null || dto.getCustomerPhone().isBlank()) {
            throw new IllegalArgumentException("Customer phone number is required");
        }

        // ── 2. Reserve stock for every item (all-or-nothing) ─────────
        List<OrderDTO.OrderItemDTO> reservedItems = new ArrayList<>();
        try {
            for (OrderDTO.OrderItemDTO itemDTO : dto.getItems()) {
                log.info("Reserving stock - productId: {}, variantId: {}, qty: {}",
                        itemDTO.getProductId(), itemDTO.getVariantId(), itemDTO.getQuantity());

                boolean reserved = inventoryService.reserveStock(
                        itemDTO.getProductId(),
                        itemDTO.getVariantId(),
                        itemDTO.getQuantity()
                );

                if (!reserved) {
                    // rollback already-reserved items before throwing
                    reservedItems.forEach(r -> inventoryService.releaseReservation(
                            r.getProductId(), r.getVariantId(), r.getQuantity()));

                    throw new RuntimeException("Insufficient stock for " +
                            (itemDTO.getVariantId() != null
                                    ? "variant: " + itemDTO.getVariantId()
                                    : "product: " + itemDTO.getProductId()));
                }
                reservedItems.add(itemDTO);
            }
        } catch (RuntimeException ex) {
            // release any partial reservations if findInventory itself throws
            reservedItems.forEach(r -> inventoryService.releaseReservation(
                    r.getProductId(), r.getVariantId(), r.getQuantity()));
            throw ex;
        }

        // ── 3. Build the Order ───────────────────────────────────────
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customerPhone(dto.getCustomerPhone().trim())
                .customerEmail(dto.getCustomerEmail() != null
                        && !dto.getCustomerEmail().isBlank()
                        ? dto.getCustomerEmail().trim() : null)
                .customerName(dto.getCustomerName())
                .status(OrderStatus.CONFIRMED)
                .shippingAddress(dto.getShippingAddress())
                .billingAddress(dto.getBillingAddress())
                .shippingCost(dto.getShippingCost() != null
                        ? dto.getShippingCost() : BigDecimal.ZERO)
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "TND")
                .build();

        // ── 4. Build Order Items ─────────────────────────────────────
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderDTO.OrderItemDTO itemDTO : dto.getItems()) {

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + itemDTO.getProductId()));

            // resolve variant (null for simple products)
            ProductVariant variant = null;
            if (itemDTO.getVariantId() != null) {
                variant = variantRepository.findById(itemDTO.getVariantId())
                        .orElseThrow(() -> new RuntimeException(
                                "Variant not found: " + itemDTO.getVariantId()));
            }

            BigDecimal unitPrice = product.getPricing().getSalePrice() != null
                    ? product.getPricing().getSalePrice()
                    : product.getPricing().getBasePrice();

            BigDecimal totalPrice = unitPrice.multiply(
                    BigDecimal.valueOf(itemDTO.getQuantity()));

            subtotal = subtotal.add(totalPrice);

            order.addItem(OrderItem.builder()
                    .product(product)
                    .variant(variant)               
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(totalPrice)
                    .build());
        }

        // ── 5. Totals ────────────────────────────────────────────────
        BigDecimal tax      = BigDecimal.ZERO;
        BigDecimal shipping = order.getShippingCost();

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(subtotal.add(tax).add(shipping));

        Order savedOrder = orderRepository.save(order);

        // ── 6. Confirm reservations ──────────────────────────────────
        for (OrderItem item : savedOrder.getItems()) {
            inventoryService.confirmReservation(
                    item.getProduct().getId(),
                    item.getVariant() != null ? item.getVariant().getId() : null, 
                    item.getQuantity()
            );
        }

        // ── 7. Optional confirmation email ───────────────────────────
        boolean deferConfirmationEmail = Boolean.TRUE.equals(dto.getDeferConfirmationEmail());

        if (savedOrder.getCustomerEmail() != null && !deferConfirmationEmail) {
        try {
                mailService.sendOrderConfirmation(emailMapper.toEmailDto(savedOrder));
        } catch (Exception e) {
                System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
        }

        // ── Always notify store owner ─────────────────────────────────
        if (savedOrder.getCustomerEmail() != null) {
        try {
                mailService.sendInternalOrderNotification(emailMapper.toEmailDto(savedOrder));
        } catch (Exception e) {
                log.warn("Failed to send internal order notification: {}", e.getMessage());
        }
        }

        return mapToDTO(savedOrder);
    }

    // ─────────────────────────────────────────────────────────────────
    // Queries
    // ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        return mapToDTO(orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderByNumber(String orderNumber) {
        return mapToDTO(orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus prevStatus = order.getStatus();
        order.setStatus(status);
        Order updated = orderRepository.save(order);

        if (updated.getCustomerEmail() != null) {
            try {
                mailService.sendOrderStatusUpdate(updated, prevStatus);
            } catch (Exception e) {
                log.error("Failed to send status update email: {}", e.getMessage());
            }
        }
        return mapToDTO(updated);
    }

    // ─────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private OrderDTO mapToDTO(Order order) {
        List<OrderDTO.OrderItemDTO> items = order.getItems().stream()
                .map(item -> OrderDTO.OrderItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .variantId(item.getVariant() != null    
                                ? item.getVariant().getId() : null)
                        .productName(item.getProduct().getResolvedName("en"))
                        .selectedSize(item.getSelectedSize())   
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerPhone(order.getCustomerPhone())
                .customerEmail(order.getCustomerEmail())
                .customerName(order.getCustomerName())
                .status(order.getStatus())
                .items(items)
                .subtotal(order.getSubtotal())
                .shippingCost(order.getShippingCost())
                .tax(order.getTax())
                .discountAmount(order.getDiscountAmount())
                .appliedDiscountCode(order.getAppliedDiscountCode())
                .total(order.getTotal())
                .currency(order.getCurrency())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}