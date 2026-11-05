package com.ecommerce.mapper;

import com.ecommerce.config.AppProperties;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderItem;
import com.ecommerce.domain.ProductAsset;
import com.ecommerce.dto.OrderEmailDTO;
import com.ecommerce.dto.OrderEmailDTO.OrderItemEmailDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEmailMapper {

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String EMAIL_LANGUAGE = "fr";
    private final AppProperties appProperties;

    /**
     * Converts Order entity → flat DTO for email template.
     * Resolves i18n names, primary images, and formats dates.
     * Must be called WITHIN a transaction (items/product are lazy).
     */
    public OrderEmailDTO toEmailDto(Order order) {
        return OrderEmailDTO.builder()
            // Customer
            .name(order.getCustomerName())
            .email(order.getCustomerEmail())

            // Order
            .orderNumber(order.getOrderNumber())
            .orderDate(order.getCreatedAt() != null
                ? order.getCreatedAt().format(DATE_FORMAT)
                : "")
            .deliveryAddress(order.getShippingAddress() != null
                ? order.getShippingAddress()
                : "Non renseignée")

            // Pricing
            .subtotal(order.getSubtotal())
            .shipping(order.getShippingCost())
            .total(order.getTotal())

            // Discount
            .discountCode(order.getAppliedDiscountCode())
            .discountAmount(order.getDiscountAmount())

            // Items
            .orderItems(mapItems(order.getItems()))

            // Company
            .companyName(appProperties.getCompanyName())
            .supportEmail(appProperties.getSupportEmail())
            .frontendUrl(appProperties.getFrontendUrl())

            .build();
    }

    private List<OrderItemEmailDTO> mapItems(List<OrderItem> items) {
        return items.stream()
            .map(this::mapItem)
            .toList();
    }

    private OrderItemEmailDTO mapItem(OrderItem item) {
        return OrderItemEmailDTO.builder()
            .productName(resolveProductName(item))
            .imageUrl(resolvePrimaryImage(item))
            .quantity(item.getQuantity())
            .price(item.getUnitPrice())
            .build();
    }

    /**
     * Resolves product name via i18n attributes.
     * Falls back to nameKey if no translation found.
     */
    private String resolveProductName(OrderItem item) {
        if (item.getProduct() == null) {
            return "Produit inconnu";
        }
        return item.getProduct().getResolvedName(EMAIL_LANGUAGE);
    }

    /**
     * Finds the primary image URL from product assets.
     * Falls back to first asset, then placeholder.
     */
    private String resolvePrimaryImage(OrderItem item) {
        if (item.getProduct() == null || item.getProduct().getAssets() == null) {
            return "";
        }

        return item.getProduct().getAssets().stream()
            .filter(asset -> Boolean.TRUE.equals(asset.getIsPrimary()))
            .findFirst()
            .map(ProductAsset::getUrl)
            .orElseGet(() -> item.getProduct().getAssets().stream()
                .findFirst()
                .map(ProductAsset::getUrl)
                .orElse(""));
    }
}