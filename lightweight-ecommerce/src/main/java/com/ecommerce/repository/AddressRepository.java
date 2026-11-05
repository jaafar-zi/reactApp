// src/main/java/com/yourcompany/ecommerce/repository/AddressRepository.java
package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerEmail(String customerEmail);
    List<Address> findByCustomerEmailAndType(String customerEmail, Address.AddressType type);
    Optional<Address> findByCustomerEmailAndIsDefaultTrue(String customerEmail);
    Optional<Address> findByCustomerEmailAndTypeAndIsDefaultTrue(String customerEmail, Address.AddressType type);
    boolean existsByCustomerEmailAndIsDefaultTrue(String customerEmail);
}