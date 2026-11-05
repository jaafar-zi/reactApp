// src/main/java/com/yourcompany/ecommerce/repository/ShippingRepository.java
package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.Shipping;
import com.ecommerce.domain.ShippingStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    Optional<Shipping> findByTrackingNumber(String trackingNumber);
    Optional<Shipping> findByOrderId(Long orderId);
    
    @Query("SELECT s FROM Shipping s LEFT JOIN FETCH s.statusHistory WHERE s.trackingNumber = :trackingNumber")
    Optional<Shipping> findByTrackingNumberWithHistory(String trackingNumber);
    
    List<Shipping> findByCurrentStatus(ShippingStatus.Status status);
    List<Shipping> findByCarrier(String carrier);
    
    @Query("SELECT s FROM Shipping s WHERE s.order.customerEmail = :email ORDER BY s.createdAt DESC")
    List<Shipping> findByCustomerEmail(String email);
}