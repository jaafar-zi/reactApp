package com.ecommerce.web;

import com.ecommerce.domain.User;
import com.ecommerce.dto.ChangePasswordRequest;
import com.ecommerce.dto.ProfileResponse;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final AuthService authService;

    /**
     * GET /api/auth/profile
     * Returns the authenticated user's profile.
     */
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.getProfile(user));
    }

    /**
     * PUT /api/auth/profile
     * Updates personal info (firstName, lastName, email).
     * Only non-null fields are updated.
     */
    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(authService.updateProfile(user, request));
    }

    /**
     * PATCH /api/auth/profile/password
     * Changes the user's password.
     * Requires the current password for verification.
     */
    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(user, request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}