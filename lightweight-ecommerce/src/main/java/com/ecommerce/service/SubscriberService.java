package com.ecommerce.service;

import com.ecommerce.config.AppProperties;
import com.ecommerce.domain.DiscountCode;
import com.ecommerce.domain.DiscountType;
import com.ecommerce.domain.Subscriber;
import com.ecommerce.dto.*;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.DiscountCodeRepository;
import com.ecommerce.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriberService {

    private final SubscriberRepository   subscriberRepository;
    private final DiscountCodeService    discountCodeService;
    private final DiscountCodeRepository discountCodeRepository;
    private final MailService            mailService;
    private final AppProperties          appProperties;

    // Default discount settings for community join
    private static final DiscountType DEFAULT_DISCOUNT_TYPE  = DiscountType.PERCENTAGE;
    private static final BigDecimal   DEFAULT_DISCOUNT_VALUE = BigDecimal.valueOf(10);
    private static final int          DEFAULT_MAX_USES       = 1;
    private static final int          DEFAULT_MAX_USES_PER_CUSTOMER = 1;


    @Transactional
    public JoinCommunityResponse joinCommunity(JoinCommunityRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String name  = request.getName().trim();
        String prefix = request.getPrefix().trim().toUpperCase();

        Optional<Subscriber> existing = subscriberRepository.findByEmail(email);

        // ── Already subscribed and active ─────────────────────────
        if (existing.isPresent() && Boolean.TRUE.equals(existing.get().getActive())) {
            Subscriber subscriber = existing.get();

            // Find their existing discount code (if any)
            String existingCode = findExistingDiscountCode(prefix, email);

            if (existingCode != null) {
                // Already has a code — return it, don't create duplicate
                log.info("Returning existing discount code for: {}", email);

                return JoinCommunityResponse.builder()
                        .subscriberId(subscriber.getId())
                        .name(subscriber.getName())
                        .email(subscriber.getEmail())
                        .isNewSubscriber(false)
                        .discountCode(existingCode)
                        .discountDescription(formatDiscountDescription())
                        .build();
            }

            // Active subscriber but no code yet — generate one
            DiscountCode code = createCommunityDiscount(prefix);

            // Send email with discount code
            mailService.sendSubscriptionConfirmation(subscriber, code.getCode());

            log.info("Generated new discount code for existing subscriber: {}", email);

            return JoinCommunityResponse.builder()
                    .subscriberId(subscriber.getId())
                    .name(subscriber.getName())
                    .email(subscriber.getEmail())
                    .isNewSubscriber(false)
                    .discountCode(code.getCode())
                    .discountDescription(formatDiscountDescription())
                    .build();
        }

        // ── Previously unsubscribed — reactivate ──────────────────
        if (existing.isPresent()) {
            Subscriber subscriber = existing.get();
            subscriber.setActive(true);
            subscriber.setUnsubscribedAt(null);
            subscriber.setName(name);
            subscriberRepository.save(subscriber);

            DiscountCode code = createCommunityDiscount(prefix);
            mailService.sendSubscriptionConfirmation(subscriber, code.getCode());

            log.info("Re-subscribed with discount code: {}", email);

            return JoinCommunityResponse.builder()
                    .subscriberId(subscriber.getId())
                    .name(subscriber.getName())
                    .email(subscriber.getEmail())
                    .isNewSubscriber(false)
                    .discountCode(code.getCode())
                    .discountDescription(formatDiscountDescription())
                    .build();
        }

        // ── Brand new subscriber ──────────────────────────────────
        Subscriber newSubscriber = Subscriber.builder()
                .name(name)
                .email(email)
                .active(true)
                .build();

        Subscriber saved = subscriberRepository.save(newSubscriber);

        DiscountCode code = createCommunityDiscount(prefix);
        mailService.sendSubscriptionConfirmation(saved, code.getCode());

        log.info("New subscriber with discount code: {}", email);

        return JoinCommunityResponse.builder()
                .subscriberId(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .isNewSubscriber(true)
                .discountCode(code.getCode())
                .discountDescription(formatDiscountDescription())
                .build();
    }

    // ----------------------------------------------------------------
    // Legacy subscribe (keep for backward compatibility)
    // ----------------------------------------------------------------

    @Transactional
    public SubscriberDTO subscribe(SubscriberDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();

        subscriberRepository.findByEmail(email).ifPresent(existing -> {
            if (Boolean.TRUE.equals(existing.getActive())) {
                throw new BusinessException("This email is already subscribed.");
            }

            existing.setActive(true);
            existing.setUnsubscribedAt(null);

            if (dto.getName() != null && !dto.getName().isBlank()) {
                existing.setName(dto.getName().trim());
            }

            subscriberRepository.save(existing);
            mailService.sendSubscriptionConfirmation(existing, null);

            log.info("Re-subscribed: {}", email);
        });

        if (subscriberRepository.existsByEmail(email)) {
            Subscriber reactivated = subscriberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("Subscriber not found."));
            return mapToDTO(reactivated);
        }

        Subscriber newSubscriber = Subscriber.builder()
                .name(dto.getName() != null ? dto.getName().trim() : null)
                .email(email)
                .active(true)
                .build();

        Subscriber saved = subscriberRepository.save(newSubscriber);
        mailService.sendSubscriptionConfirmation(saved, null);

        log.info("New subscriber saved: {}", email);

        return mapToDTO(saved);
    }

    // ----------------------------------------------------------------
    // Unsubscribe
    // ----------------------------------------------------------------

    @Transactional
    public void unsubscribe(String email) {
        Subscriber subscriber = subscriberRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new BusinessException("Subscriber not found."));

        if (Boolean.FALSE.equals(subscriber.getActive())) {
            throw new BusinessException("This email is already unsubscribed.");
        }

        subscriber.setActive(false);
        subscriber.setUnsubscribedAt(LocalDateTime.now());
        subscriberRepository.save(subscriber);

        log.info("Unsubscribed: {}", email);
    }

    // ----------------------------------------------------------------
    // Queries
    // ----------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<SubscriberDTO> getAllActiveSubscribers() {
        return subscriberRepository.findAll().stream()
                .filter(Subscriber::getActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countActiveSubscribers() {
        return subscriberRepository.findAll().stream()
                .filter(Subscriber::getActive)
                .count();
    }

    // ----------------------------------------------------------------
    // Private helpers
    // ----------------------------------------------------------------

    private DiscountCode createCommunityDiscount(String prefix) {
        CreateDiscountCodeRequest discountRequest = CreateDiscountCodeRequest.builder()
                .userPrefix(prefix)
                .discountType(DEFAULT_DISCOUNT_TYPE)
                .discountValue(DEFAULT_DISCOUNT_VALUE)
                .maxUses(DEFAULT_MAX_USES)
                .maxUsesPerCustomer(DEFAULT_MAX_USES_PER_CUSTOMER)
                .build();

        return discountCodeService.createDiscountCode(discountRequest);
    }

    /**
     * Try to find an existing active discount code with this prefix.
     * Returns the code string if found, null otherwise.
     */
    private String findExistingDiscountCode(String prefix, String email) {
        return discountCodeRepository
                .findByUserPrefixAndActiveTrue(prefix)
                .map(DiscountCode::getCode)
                .orElse(null);
    }

    private String formatDiscountDescription() {
        if (DEFAULT_DISCOUNT_TYPE == DiscountType.PERCENTAGE) {
            return DEFAULT_DISCOUNT_VALUE.stripTrailingZeros().toPlainString() + "% off your first order";
        }
        return DEFAULT_DISCOUNT_VALUE.stripTrailingZeros().toPlainString() + " DT off your first order";
    }

    private SubscriberDTO mapToDTO(Subscriber subscriber) {
        return SubscriberDTO.builder()
                .id(subscriber.getId())
                .name(subscriber.getName())
                .email(subscriber.getEmail())
                .active(subscriber.getActive())
                .build();
    }
}