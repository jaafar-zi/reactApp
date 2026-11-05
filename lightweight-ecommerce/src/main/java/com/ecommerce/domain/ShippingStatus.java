package com.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_id", nullable = false)
    private Shipping shipping;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column
    private String updatedBy; // System user or employee who updated
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public enum Status {
        PENDING("Pending", "Order is being prepared"),
        PROCESSING("Processing", "Order is being processed"),
        READY_TO_SHIP("Ready to Ship", "Order is ready for shipping"),
        PICKED_UP("Picked Up", "Package has been picked up by carrier"),
        IN_TRANSIT("In Transit", "Package is on the way"),
        OUT_FOR_DELIVERY("Out for Delivery", "Package is out for delivery"),
        DELIVERED("Delivered", "Package has been delivered"),
        DELIVERY_ATTEMPTED("Delivery Attempted", "Delivery was attempted but failed"),
        RETURNED("Returned", "Package is being returned"),
        CANCELLED("Cancelled", "Shipping has been cancelled"),
        ON_HOLD("On Hold", "Shipping is temporarily on hold"),
        EXCEPTION("Exception", "An exception has occurred");
        
        private final String displayName;
        private final String description;
        
        Status(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
}