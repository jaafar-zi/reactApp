// src/main/java/com/yourcompany/ecommerce/controller/AddressController.java
package com.ecommerce.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.domain.Address;
import com.ecommerce.dto.AddressDTO;
import com.ecommerce.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AddressController {
    
    private final AddressService addressService;
    
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<AddressDTO>> getCustomerAddresses(@PathVariable String email) {
        return ResponseEntity.ok(addressService.getAddressesByCustomer(email));
    }
    
    @GetMapping("/customer/{email}/type/{type}")
    public ResponseEntity<List<AddressDTO>> getCustomerAddressesByType(
            @PathVariable String email,
            @PathVariable String type) {
        Address.AddressType addressType = Address.AddressType.valueOf(type.toUpperCase());
        return ResponseEntity.ok(addressService.getAddressesByCustomerAndType(email, addressType));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }
    
    @GetMapping("/customer/{email}/default")
    public ResponseEntity<AddressDTO> getDefaultAddress(@PathVariable String email) {
        return ResponseEntity.ok(addressService.getDefaultAddress(email));
    }
    
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.createAddress(addressDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/set-default")
    public ResponseEntity<AddressDTO> setDefaultAddress(
            @PathVariable Long id,
            @RequestParam String customerEmail) {
        return ResponseEntity.ok(addressService.setDefaultAddress(id, customerEmail));
    }
}