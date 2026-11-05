// src/main/java/com/yourcompany/ecommerce/domain/shipping/Shipping.java
package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ecommerce.domain.ShippingStatus.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shippings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String trackingNumber;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingMethod shippingMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status currentStatus;
    
    @Column(nullable = false)
    private String carrier; // e.g., "UPS", "FedEx", "DHL", "USPS"
    
    @Column(precision = 10, scale = 2)
    private BigDecimal shippingCost;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal weight; // in kg
    
    @Column
    private LocalDateTime estimatedDeliveryDate;
    
    @Column
    private LocalDateTime actualDeliveryDate;
    
    @Column
    private LocalDateTime shippedDate;
    
    @Column(columnDefinition = "TEXT")
    private String specialInstructions;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("timestamp DESC")
    @Builder.Default
    private List<ShippingStatus> statusHistory = new ArrayList<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum ShippingMethod {
        STANDARD("Standard Shipping", 5, 7),
        EXPRESS("Express Shipping", 2, 3),
        NEXT_DAY("Next Day Delivery", 1, 1),
        TWO_DAY("Two Day Delivery", 2, 2),
        INTERNATIONAL("International Shipping", 10, 21),
        PICKUP("In-Store Pickup", 0, 0);
        
        private final String displayName;
        private final int minDays;
        private final int maxDays;
        
        ShippingMethod(String displayName, int minDays, int maxDays) {
            this.displayName = displayName;
            this.minDays = minDays;
            this.maxDays = maxDays;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getMinDays() {
            return minDays;
        }
        
        public int getMaxDays() {
            return maxDays;
        }
    }
    
    // Helper methods
    public void addStatusUpdate(ShippingStatus status) {
        statusHistory.add(status);
        status.setShipping(this);
        this.currentStatus = status.getStatus();
    }
    
    public boolean isDelivered() {
        return currentStatus == ShippingStatus.Status.DELIVERED;
    }
    
    public boolean isInTransit() {
        return currentStatus == ShippingStatus.Status.IN_TRANSIT;
    }
}