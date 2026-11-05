package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false, length = 20)
    private String customerPhone;

    @Column(length = 255)
    private String customerEmail;

    @Column(nullable = false)
    private String customerName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CONFIRMED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code_id")
    private DiscountCode discountCode;

    @Column(length = 100)
    private String appliedDiscountCode;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Builder.Default
    @Column(length = 3)
    private String currency = "TND";

    private String shippingAddress;
    private String billingAddress;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void recalculateTotal() {
        BigDecimal safeSubtotal  = subtotal != null ? subtotal : BigDecimal.ZERO;
        BigDecimal safeTax       = tax != null ? tax : BigDecimal.ZERO;
        BigDecimal safeDiscount  = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        BigDecimal safeShipping  = shippingCost != null ? shippingCost : BigDecimal.ZERO;

        BigDecimal calculated = safeSubtotal
            .add(safeTax)
            .add(safeShipping)
            .subtract(safeDiscount);

        this.total = calculated.compareTo(BigDecimal.ZERO) < 0
            ? BigDecimal.ZERO
            : calculated;
    }
}