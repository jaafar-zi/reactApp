package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.ShippingStatus;

import java.util.List;

@Repository
public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long> {
    List<ShippingStatus> findByShippingIdOrderByTimestampDesc(Long shippingId);
    List<ShippingStatus> findByStatus(ShippingStatus.Status status);
}