// src/main/java/com/ecommerce/service/AddressService.java
package com.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.domain.Address;
import com.ecommerce.dto.AddressDTO;
import com.ecommerce.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    
    private final AddressRepository addressRepository;
    
    @Transactional(readOnly = true)
    public List<AddressDTO> getAddressesByCustomer(String customerEmail) {
        return addressRepository.findByCustomerEmail(customerEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AddressDTO> getAddressesByCustomerAndType(String customerEmail, Address.AddressType type) {
        return addressRepository.findByCustomerEmailAndType(customerEmail, type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return mapToDTO(address);
    }
    
    @Transactional(readOnly = true)
    public AddressDTO getDefaultAddress(String customerEmail) {
        Address address = addressRepository.findByCustomerEmailAndIsDefaultTrue(customerEmail)
                .orElseThrow(() -> new RuntimeException("No default address found"));
        return mapToDTO(address);
    }
    
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO) {
        // If this is set as default, unset other defaults
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {  // Fixed: was IsDefault()
            unsetOtherDefaults(addressDTO.getCustomerEmail());
        }
        
        Address address = Address.builder()
                .fullName(addressDTO.getFullName())
                .phoneNumber(addressDTO.getPhoneNumber())
                .addressLine1(addressDTO.getAddressLine1())
                .addressLine2(addressDTO.getAddressLine2())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .postalCode(addressDTO.getPostalCode())
                .country(addressDTO.getCountry())
                .type(Address.AddressType.valueOf(addressDTO.getType()))
                .isDefault(addressDTO.getIsDefault() != null ? addressDTO.getIsDefault() : false)
                .customerEmail(addressDTO.getCustomerEmail())
                .notes(addressDTO.getNotes())
                .build();
        
        Address saved = addressRepository.save(address);
        log.info("Created address with ID: {}", saved.getId());
        return mapToDTO(saved);
    }
    
    @Transactional
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // If setting as default, unset other defaults
        if (Boolean.TRUE.equals(addressDTO.getIsDefault()) && !address.getIsDefault()) {
            unsetOtherDefaults(address.getCustomerEmail());
        }
        
        address.setFullName(addressDTO.getFullName());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setType(Address.AddressType.valueOf(addressDTO.getType()));
        address.setIsDefault(addressDTO.getIsDefault());
        address.setNotes(addressDTO.getNotes());
        
        Address updated = addressRepository.save(address);
        log.info("Updated address with ID: {}", updated.getId());
        return mapToDTO(updated);
    }
    
    @Transactional
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found");
        }
        addressRepository.deleteById(id);
        log.info("Deleted address with ID: {}", id);
    }
    
    @Transactional
    public AddressDTO setDefaultAddress(Long id, String customerEmail) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        if (!address.getCustomerEmail().equals(customerEmail)) {
            throw new RuntimeException("Address does not belong to customer");
        }
        
        unsetOtherDefaults(customerEmail);
        
        address.setIsDefault(true);
        Address updated = addressRepository.save(address);
        log.info("Set address {} as default for customer {}", id, customerEmail);
        return mapToDTO(updated);
    }
    
    private void unsetOtherDefaults(String customerEmail) {
        List<Address> defaultAddresses = addressRepository.findByCustomerEmail(customerEmail)
                .stream()
                .filter(Address::getIsDefault)
                .collect(Collectors.toList());
        
        defaultAddresses.forEach(addr -> addr.setIsDefault(false));
        addressRepository.saveAll(defaultAddresses);
    }
    
    private AddressDTO mapToDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .type(address.getType().name())
                .isDefault(address.getIsDefault())
                .customerEmail(address.getCustomerEmail())
                .notes(address.getNotes())
                .formattedAddress(address.getFormattedAddress())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}