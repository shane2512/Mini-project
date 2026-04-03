package com.ftms.controller;

// AuthController handles login and registration endpoints.
// These are the only endpoints that are PUBLIC (no JWT needed).
// /api/auth/register - creates a new user
// /api/auth/login - validates credentials and returns JWT token

import com.ftms.dto.LoginRequest;
import com.ftms.dto.RegisterRequest;
import com.ftms.model.User;
import com.ftms.repository.UserRepository;
import com.ftms.service.JwtService;
import com.ftms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // POST /api/auth/register
    // Frontend sends registration form data, this saves it to DB with PENDING
    // status
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.registerUser(request);
            response.put("success", true);
            response.put("message", "Registration successful. Please wait for admin KYC approval.");
            response.put("userId", user.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // POST /api/auth/login
    // Frontend sends email + password, this validates and returns JWT token + user
    // info
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        // Check password matches (BCrypt comparison)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid password");
            return ResponseEntity.badRequest().body(response);
        }

        // Check KYC status - only ADMIN, CENTRAL_BANK, COMMERCIAL_BANK skip KYC check
        if (user.getKycStatus() == User.KycStatus.PENDING
                && user.getRole() != User.Role.ADMIN
                && user.getRole() != User.Role.CENTRAL_BANK
                && user.getRole() != User.Role.COMMERCIAL_BANK) {
            response.put("success", false);
            response.put("message", "Your KYC verification is pending. Please wait for admin approval.");
            return ResponseEntity.badRequest().body(response);
        }

        if (user.getKycStatus() == User.KycStatus.REJECTED) {
            response.put("success", false);
            response.put("message", "Your KYC was rejected. Please contact support.");
            return ResponseEntity.badRequest().body(response);
        }

        // Generate JWT token with email and role
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        response.put("success", true);
        response.put("token", token);
        response.put("role", user.getRole().name());
        response.put("userId", user.getId());
        response.put("fullName", user.getFullName());
        response.put("roleSelected", user.getRoleSelected());
        response.put("kycStatus", user.getKycStatus().name());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }
}
