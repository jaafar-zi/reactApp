package com.ecommerce.service;

import com.ecommerce.domain.DiscountCode;
import com.ecommerce.domain.DiscountType;
import com.ecommerce.domain.Order;
import com.ecommerce.dto.CreateDiscountCodeRequest;
import com.ecommerce.mapper.OrderEmailMapper;
import com.ecommerce.repository.DiscountCodeRepository;
import com.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountCodeService {

    private static final String SYSTEM_PREFIX = "JWST";
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final DiscountCodeRepository discountCodeRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;
    private final OrderEmailMapper emailMapper; 

    private final SecureRandom secureRandom = new SecureRandom();

    // ─── Validate (read-only, no side effects) ───────────────────

    /**
     * Validates a discount code without applying it to any order.
     * Returns the full DiscountCode if valid.
     * Throws IllegalArgumentException if not found.
     * Throws IllegalStateException if inactive, expired, or maxed out.
     */
    @Transactional(readOnly = true)
    public DiscountCode validateCode(String rawCode) {
        if (rawCode == null || rawCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Discount code cannot be empty");
        }

        DiscountCode discountCode = discountCodeRepository
                .findByCodeIgnoreCase(rawCode.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Discount code not found"
                ));

        if (!Boolean.TRUE.equals(discountCode.getActive())) {
            throw new IllegalStateException("Discount code is no longer active");
        }

        if (discountCode.getExpiresAt() != null
                && discountCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Discount code has expired");
        }

        if (discountCode.getMaxUses() != null
                && discountCode.getCurrentUses() != null
                && discountCode.getCurrentUses() >= discountCode.getMaxUses()) {
            throw new IllegalStateException("Discount code has reached its usage limit");
        }

        return discountCode;
    }

    // ─── Create ──────────────────────────────────────────────────

    @Transactional
    public DiscountCode createDiscountCode(CreateDiscountCodeRequest request) {
        validateCreateRequest(request);

        String normalizedPrefix = normalizePrefix(request.getUserPrefix());
        if (normalizedPrefix.isBlank()) {
            throw new IllegalArgumentException("User prefix must contain letters or numbers");
        }

        String code = generateUniqueCode(normalizedPrefix);

        DiscountCode discountCode = DiscountCode.builder()
                .code(code)
                .userPrefix(normalizedPrefix)
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue().setScale(2, RoundingMode.HALF_UP))
                .minimumOrderAmount(request.getMinimumOrderAmount() != null
                        ? request.getMinimumOrderAmount().setScale(2, RoundingMode.HALF_UP)
                        : null)
                .maxUses(request.getMaxUses())
                .currentUses(0)
                .maxUsesPerCustomer(request.getMaxUsesPerCustomer())
                .expiresAt(request.getExpiresAt())
                .active(true)
                .build();

        return discountCodeRepository.save(discountCode);
    }

    // ─── Apply ───────────────────────────────────────────────────

    @Transactional
    public Order applyDiscountCode(Long orderId, String rawCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getDiscountCode() != null ||
            (order.getDiscountAmount() != null && order.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0)) {
            throw new IllegalStateException("A discount code is already applied to this order");
        }

        DiscountCode discountCode = discountCodeRepository.findByCodeIgnoreCase(rawCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Discount code not found"));

        validateDiscountForOrder(discountCode, order);

        BigDecimal discountAmount = calculateDiscountAmount(discountCode, order);

        order.setDiscountCode(discountCode);
        order.setAppliedDiscountCode(discountCode.getCode());
        order.setDiscountAmount(discountAmount);
        order.recalculateTotal();

        int currentUses = discountCode.getCurrentUses() == null ? 0 : discountCode.getCurrentUses();
        discountCode.setCurrentUses(currentUses + 1);

        discountCodeRepository.save(discountCode);
        orderRepository.save(order);

        if (order.getCustomerEmail() != null) {
            try {
                mailService.sendOrderConfirmation(
                    emailMapper.toEmailDto(order)
                );
            } catch (Exception e) {
                System.err.println(
                    "Failed to resend confirmation email: " + e.getMessage()
                );
            }
        }

        return order;
    }

    // ─── Private: Validation ─────────────────────────────────────

    private void validateCreateRequest(CreateDiscountCodeRequest request) {
        if (request.getDiscountValue() == null || request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than 0");
        }

        if (request.getDiscountType() == DiscountType.PERCENTAGE
                && request.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot be greater than 100");
        }

        if (request.getMinimumOrderAmount() != null
                && request.getMinimumOrderAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum order amount cannot be negative");
        }

        if (request.getMaxUses() != null && request.getMaxUses() < 1) {
            throw new IllegalArgumentException("Max uses must be at least 1");
        }

        if (request.getMaxUsesPerCustomer() != null && request.getMaxUsesPerCustomer() < 1) {
            throw new IllegalArgumentException("Max uses per customer must be at least 1");
        }

        if (request.getExpiresAt() != null && request.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past");
        }
    }

    private void validateDiscountForOrder(DiscountCode discountCode, Order order) {
        if (!Boolean.TRUE.equals(discountCode.getActive())) {
            throw new IllegalStateException("Discount code is inactive");
        }

        if (discountCode.getExpiresAt() != null && discountCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Discount code has expired");
        }

        if (discountCode.getMaxUses() != null &&
                discountCode.getCurrentUses() != null &&
                discountCode.getCurrentUses() >= discountCode.getMaxUses()) {
            throw new IllegalStateException("Discount code usage limit reached");
        }

        if (discountCode.getMinimumOrderAmount() != null &&
                order.getSubtotal().compareTo(discountCode.getMinimumOrderAmount()) < 0) {
            throw new IllegalStateException("Order does not meet minimum amount for this discount code");
        }

        if (discountCode.getMaxUsesPerCustomer() != null) {
            long customerUsageCount = orderRepository.countByDiscountCode_IdAndCustomerEmail(
                    discountCode.getId(), order.getCustomerEmail());

            if (customerUsageCount >= discountCode.getMaxUsesPerCustomer()) {
                throw new IllegalStateException("Customer usage limit reached for this discount code");
            }
        }
    }

    // ─── Private: Calculation ────────────────────────────────────

    private BigDecimal calculateDiscountAmount(DiscountCode discountCode, Order order) {
        BigDecimal subtotal = order.getSubtotal() != null ? order.getSubtotal() : BigDecimal.ZERO;
        if (subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;

        if (discountCode.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = subtotal.multiply(discountCode.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = discountCode.getDiscountValue().setScale(2, RoundingMode.HALF_UP);
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        return discount;
    }

    // ─── Private: Code Generation ────────────────────────────────

    private String generateUniqueCode(String userPrefix) {
        String candidate;

        do {
            candidate = SYSTEM_PREFIX + "-" + userPrefix + "-" + randomSuffix(6);
        } while (discountCodeRepository.existsByCodeIgnoreCase(candidate));

        return candidate;
    }

    private String normalizePrefix(String input) {
        return input == null ? "" : input.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(secureRandom.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }
}