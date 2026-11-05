package com.ecommerce.web;

import com.ecommerce.dto.JoinCommunityRequest;
import com.ecommerce.dto.JoinCommunityResponse;
import com.ecommerce.dto.SubscriberDTO;
import com.ecommerce.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    /**
     * POST /api/subscribers/join
     *
     * Unified endpoint:
     *   1. Creates subscriber (or reactivates)
     *   2. Generates discount code
     *   3. Sends welcome email WITH discount code
     *   4. Returns everything in one response
     */
    @PostMapping("/join")
    public ResponseEntity<JoinCommunityResponse> joinCommunity(
            @Valid @RequestBody JoinCommunityRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscriberService.joinCommunity(request));
    }

    /**
     * Legacy subscribe (no discount code).
     * Keep for backward compatibility.
     */
    @PostMapping
    public ResponseEntity<SubscriberDTO> subscribe(
            @Valid @RequestBody SubscriberDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscriberService.subscribe(dto));
    }

    @DeleteMapping("/unsubscribe/{email}")
    public ResponseEntity<Void> unsubscribe(@PathVariable String email) {
        subscriberService.unsubscribe(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriberDTO>> getAllActiveSubscribers() {
        return ResponseEntity.ok(subscriberService.getAllActiveSubscribers());
    }
}