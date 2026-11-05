package com.ecommerce.service;

import com.ecommerce.domain.Role;
import com.ecommerce.domain.User;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.ChangePasswordRequest;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.ProfileResponse;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already registered: " + request.email());
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("User not found"));

        String token = jwtService.generateToken(user);
        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

        // ─── Profile ────────────────────────────────────────────

    public ProfileResponse getProfile(User user) {
        return toProfileResponse(user);
    }

    @Transactional
    public ProfileResponse updateProfile(User user, UpdateProfileRequest request) {

        // ── First name ──
        if (request.firstName() != null && !request.firstName().isBlank()) {
            user.setFirstName(request.firstName().trim());
        }

        // ── Last name ──
        if (request.lastName() != null && !request.lastName().isBlank()) {
            user.setLastName(request.lastName().trim());
        }

        // ── Email (with uniqueness check) ──
        if (request.email() != null && !request.email().isBlank()) {
            String newEmail = request.email().trim().toLowerCase();

            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new BusinessException("Email already in use: " + newEmail);
            }
            user.setEmail(newEmail);
        }

        userRepository.save(user);
        log.info("Profile updated for user: {}", user.getEmail());

        return toProfileResponse(user);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {

        // Verify current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }

        // Prevent reusing the same password
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BusinessException("New password must be different from the current one");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getEmail());
    }

    // ─── Helper ─────────────────────────────────────────────

    private ProfileResponse toProfileResponse(User user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}