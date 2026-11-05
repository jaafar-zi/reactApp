package com.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.domain.Address;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.Shipping;
import com.ecommerce.domain.ShippingStatus;
import com.ecommerce.dto.CreateShippingRequest;
import com.ecommerce.dto.ShippingDTO;
import com.ecommerce.dto.ShippingStatusDTO;
import com.ecommerce.dto.ShippingStatusUpdateRequest;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ShippingRepository;
import com.ecommerce.repository.ShippingStatusRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {
    
    private final ShippingRepository shippingRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final MailService emailService;
    
    @Transactional
    public ShippingDTO createShipping(CreateShippingRequest request) {
        // Validate order exists
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Validate address exists
        Address address = addressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // Check if shipping already exists for this order
        if (shippingRepository.findByOrderId(order.getId()).isPresent()) {
            throw new RuntimeException("Shipping already exists for this order");
        }
        
        // Create shipping
        Shipping shipping = Shipping.builder()
                .trackingNumber(generateTrackingNumber())
                .order(order)
                .shippingAddress(address)
                .shippingMethod(Shipping.ShippingMethod.valueOf(request.getShippingMethod()))
                .currentStatus(ShippingStatus.Status.PENDING)
                .carrier(request.getCarrier())
                .shippingCost(request.getShippingCost())
                .weight(request.getWeight())
                .specialInstructions(request.getSpecialInstructions())
                .notes(request.getNotes())
                .estimatedDeliveryDate(calculateEstimatedDelivery(request.getShippingMethod()))
                .build();
        
        // Create initial status
        ShippingStatus initialStatus = ShippingStatus.builder()
                .status(ShippingStatus.Status.PENDING)
                .location("Warehouse")
                .timestamp(LocalDateTime.now())
                .description("Shipping label created")
                .updatedBy("SYSTEM")
                .build();
        
        shipping.addStatusUpdate(initialStatus);
        
        Shipping saved = shippingRepository.save(shipping);
        log.info("Created shipping with tracking number: {}", saved.getTrackingNumber());
        
        
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public ShippingDTO getShippingByTrackingNumber(String trackingNumber) {
        Shipping shipping = shippingRepository.findByTrackingNumberWithHistory(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        return mapToDTO(shipping);
    }
    
    @Transactional(readOnly = true)
    public ShippingDTO getShippingByOrderId(Long orderId) {
        Shipping shipping = shippingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipping not found for order"));
        return mapToDTO(shipping);
    }
    
    @Transactional(readOnly = true)
    public List<ShippingDTO> getShippingsByCustomer(String email) {
        return shippingRepository.findByCustomerEmail(email).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ShippingDTO> getShippingsByStatus(String status) {
        ShippingStatus.Status shippingStatus = ShippingStatus.Status.valueOf(status);
        return shippingRepository.findByCurrentStatus(shippingStatus).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ShippingDTO updateShippingStatus(String trackingNumber, ShippingStatusUpdateRequest request) {
        Shipping shipping = shippingRepository.findByTrackingNumberWithHistory(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        
        ShippingStatus newStatus = ShippingStatus.builder()
                .status(ShippingStatus.Status.valueOf(request.getStatus()))
                .location(request.getLocation())
                .timestamp(LocalDateTime.now())
                .description(request.getDescription())
                .notes(request.getNotes())
                .updatedBy(request.getUpdatedBy() != null ? request.getUpdatedBy() : "SYSTEM")
                .build();
        
        shipping.addStatusUpdate(newStatus);
        
        // Update shipped date if status is PICKED_UP
        if (newStatus.getStatus() == ShippingStatus.Status.PICKED_UP && shipping.getShippedDate() == null) {
            shipping.setShippedDate(LocalDateTime.now());
        }
        
        // Update actual delivery date if status is DELIVERED
        if (newStatus.getStatus() == ShippingStatus.Status.DELIVERED) {
            shipping.setActualDeliveryDate(LocalDateTime.now());
        }
        
        Shipping updated = shippingRepository.save(shipping);
        log.info("Updated shipping status for tracking number: {} to {}", trackingNumber, request.getStatus());
        
        return mapToDTO(updated);
    }
    
    @Transactional
    public ShippingDTO updateShipping(String trackingNumber, ShippingDTO shippingDTO) {
        Shipping shipping = shippingRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        
        if (shippingDTO.getShippingCost() != null) {
            shipping.setShippingCost(shippingDTO.getShippingCost());
        }
        if (shippingDTO.getWeight() != null) {
            shipping.setWeight(shippingDTO.getWeight());
        }
        if (shippingDTO.getSpecialInstructions() != null) {
            shipping.setSpecialInstructions(shippingDTO.getSpecialInstructions());
        }
        if (shippingDTO.getNotes() != null) {
            shipping.setNotes(shippingDTO.getNotes());
        }
        if (shippingDTO.getEstimatedDeliveryDate() != null) {
            shipping.setEstimatedDeliveryDate(shippingDTO.getEstimatedDeliveryDate());
        }
        
        Shipping updated = shippingRepository.save(shipping);
        log.info("Updated shipping: {}", trackingNumber);
        return mapToDTO(updated);
    }
    
    private String generateTrackingNumber() {
        return "TRK-" + LocalDateTime.now().getYear() + "-" + 
               UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
    
    private LocalDateTime calculateEstimatedDelivery(String methodName) {
        Shipping.ShippingMethod method = Shipping.ShippingMethod.valueOf(methodName);
        return LocalDateTime.now().plusDays(method.getMaxDays());
    }
    
    private ShippingDTO mapToDTO(Shipping shipping) {
        return ShippingDTO.builder()
                .id(shipping.getId())
                .trackingNumber(shipping.getTrackingNumber())
                .orderId(shipping.getOrder().getId())
                .orderNumber(shipping.getOrder().getOrderNumber())
                .shippingAddress(addressService.getAddressById(shipping.getShippingAddress().getId()))
                .shippingMethod(shipping.getShippingMethod().name())
                .currentStatus(shipping.getCurrentStatus().name())
                .carrier(shipping.getCarrier())
                .shippingCost(shipping.getShippingCost())
                .weight(shipping.getWeight())
                .estimatedDeliveryDate(shipping.getEstimatedDeliveryDate())
                .actualDeliveryDate(shipping.getActualDeliveryDate())
                .shippedDate(shipping.getShippedDate())
                .specialInstructions(shipping.getSpecialInstructions())
                .notes(shipping.getNotes())
                .statusHistory(shipping.getStatusHistory().stream()
                        .map(this::mapStatusToDTO)
                        .collect(Collectors.toList()))
                .createdAt(shipping.getCreatedAt())
                .updatedAt(shipping.getUpdatedAt())
                .build();
    }
    
    private ShippingStatusDTO mapStatusToDTO(ShippingStatus status) {
        return ShippingStatusDTO.builder()
                .id(status.getId())
                .shippingId(status.getShipping().getId())
                .status(status.getStatus().name())
                .location(status.getLocation())
                .timestamp(status.getTimestamp())
                .description(status.getDescription())
                .notes(status.getNotes())
                .updatedBy(status.getUpdatedBy())
                .createdAt(status.getCreatedAt())
                .build();
    }
}
