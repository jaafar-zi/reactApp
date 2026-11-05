package com.ecommerce.web;

import com.ecommerce.dto.OrderEmailDTO;
import com.ecommerce.dto.OrderEmailDTO.OrderItemEmailDTO;
import com.ecommerce.service.MailService;
import com.ecommerce.service.MailService.MailTestResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Mail", description = "Mail service testing endpoints")
public class MailTestController {

    private final MailService mailService;

    // =========================================================
    // BASIC TEST
    // =========================================================

    @PostMapping("/test")
    @Operation(summary = "Send a basic test email to verify mail configuration")
    public ResponseEntity<Map<String, Object>> sendTestEmail(
            @RequestParam
            @Email(message = "Must be a valid email address")
            @Parameter(description = "Recipient email address")
            String to) {

        log.info("Test email requested for: {}", to);

        MailTestResult result = mailService.sendTestEmail(to);

        return buildResponse(result);
    }

    // =========================================================
    // ORDER CONFIRMATION TEST
    // =========================================================

    @PostMapping("/test/order-confirmation")
    @Operation(summary = "Send a test order confirmation email with mock data")
    public ResponseEntity<Map<String, Object>> testOrderConfirmation(
            @RequestParam
            @Email(message = "Must be a valid email address")
            @Parameter(description = "Recipient email address")
            String to,

            @RequestParam(required = false, defaultValue = "Jaafer Alouini")
            @Parameter(description = "Customer name for the test email")
            String customerName,

            @RequestParam(required = false, defaultValue = "true")
            @Parameter(description = "Include a discount code in the test email")
            boolean withDiscount) {

        log.info("Order confirmation test email requested for: {}", to);

        try {
            // ✅ Build mock order DTO with realistic Jawista data
            OrderEmailDTO emailDto = buildMockOrderEmail(to, customerName, withDiscount);

            // ✅ Call synchronously for test (not @Async) so we can return result
            mailService.sendOrderConfirmationSync(emailDto);

            return buildResponse(MailTestResult.success(to));

        } catch (Exception e) {
            log.error("Failed to send test order confirmation email to: {}", to, e);
            return buildResponse(MailTestResult.failure(to, e.getMessage()));
        }
    }

    // =========================================================
    // ORDER CONFIRMATION TEST - MULTI ITEM
    // =========================================================

    @PostMapping("/test/order-confirmation-multi")
    @Operation(summary = "Send a test order confirmation email with multiple items")
    public ResponseEntity<Map<String, Object>> testOrderConfirmationMultiItem(
            @RequestParam
            @Email(message = "Must be a valid email address")
            @Parameter(description = "Recipient email address")
            String to) {

        log.info("Multi-item order confirmation test email requested for: {}", to);

        try {
            OrderEmailDTO emailDto = buildMockMultiItemOrderEmail(to);

            mailService.sendOrderConfirmationSync(emailDto);

            return buildResponse(MailTestResult.success(to));

        } catch (Exception e) {
            log.error("Failed to send test multi-item order email to: {}", to, e);
            return buildResponse(MailTestResult.failure(to, e.getMessage()));
        }
    }

    // =========================================================
    // MOCK DATA BUILDERS
    // =========================================================

    private OrderEmailDTO buildMockOrderEmail(String to, String customerName, boolean withDiscount) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Single item order
        OrderItemEmailDTO item = OrderItemEmailDTO.builder()
            .productName("Tshirt Chill Bro")
            .imageUrl("https://via.placeholder.com/120x120/f5f5f5/E74C3C?text=Jawista")
            .quantity(1)
            .price(new BigDecimal("47.00"))
            .build();

        BigDecimal subtotal = new BigDecimal("47.00");
        BigDecimal shipping = new BigDecimal("7.00");
        BigDecimal total = withDiscount
            ? new BigDecimal("51.00")   // 47 + 7 - 3 (discount)
            : new BigDecimal("54.00");  // 47 + 7

        return OrderEmailDTO.builder()
            .name(customerName)
            .email(to)
            .orderNumber("#JA001")
            .orderDate(now)
            .deliveryAddress("Sahabi 4, Venezuela, Kairouan")
            .orderItems(List.of(item))
            .subtotal(subtotal)
            .shipping(shipping)
            .total(total)
            .discountCode(withDiscount ? "JAWON300" : null)
            .companyName("Jawista")
            .supportEmail("support@jawista.com")
            .frontendUrl("https://www.jawista.com")
            .build();
    }

    private OrderEmailDTO buildMockMultiItemOrderEmail(String to) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        OrderItemEmailDTO item1 = OrderItemEmailDTO.builder()
            .productName("Tshirt Chill Bro")
            .imageUrl("https://via.placeholder.com/120x120/f5f5f5/E74C3C?text=ChillBro")
            .quantity(1)
            .price(new BigDecimal("47.00"))
            .build();

        OrderItemEmailDTO item2 = OrderItemEmailDTO.builder()
            .productName("Hoodie Stay Cool")
            .imageUrl("https://via.placeholder.com/120x120/f5f5f5/333333?text=StayCool")
            .quantity(2)
            .price(new BigDecimal("89.00"))
            .build();

        OrderItemEmailDTO item3 = OrderItemEmailDTO.builder()
            .productName("Cap Classic")
            .imageUrl("https://via.placeholder.com/120x120/f5f5f5/FFD700?text=Cap")
            .quantity(1)
            .price(new BigDecimal("25.00"))
            .build();

        // 47 + (89*2) + 25 = 250
        BigDecimal subtotal = new BigDecimal("250.00");
        BigDecimal shipping = new BigDecimal("7.00");
        BigDecimal total = new BigDecimal("257.00");

        return OrderEmailDTO.builder()
            .name("Jaafer Alouini")
            .email(to)
            .orderNumber("#JA002")
            .orderDate(now)
            .deliveryAddress("Avenue Habib Bourguiba, Tunis 1000")
            .orderItems(List.of(item1, item2, item3))
            .subtotal(subtotal)
            .shipping(shipping)
            .total(total)
            .discountCode(null)
            .companyName("Jawista")
            .supportEmail("support@jawista.com")
            .frontendUrl("https://www.jawista.com")
            .build();
    }

    // =========================================================
    // RESPONSE BUILDER
    // =========================================================

    private ResponseEntity<Map<String, Object>> buildResponse(MailTestResult result) {
        Map<String, Object> response = Map.of(
            "success",   result.success(),
            "recipient", result.recipient(),
            "message",   result.message(),
            "timestamp", result.timestamp().toString()
        );

        return result.success()
            ? ResponseEntity.ok(response)
            : ResponseEntity.internalServerError().body(response);
    }
}