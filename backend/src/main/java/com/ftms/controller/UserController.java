package com.ftms.controller;

import com.ftms.model.User;
import com.ftms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // PUT /api/user/select-role
    // Allows a user to select their role after KYC approval
    @PutMapping("/select-role")
    public ResponseEntity<Map<String, Object>> selectRole(
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        Map<String, Object> resp = new HashMap<>();

        try {
            String userEmail = authentication.getName();
            Optional<User> userOpt = userRepository.findByEmail(userEmail);

            if (userOpt.isEmpty()) {
                resp.put("success", false);
                resp.put("message", "User not found");
                return ResponseEntity.badRequest().body(resp);
            }

            User user = userOpt.get();

            // Validate KYC is approved
            if (user.getKycStatus() != User.KycStatus.APPROVED) {
                resp.put("success", false);
                resp.put("message", "KYC must be approved before selecting role");
                return ResponseEntity.badRequest().body(resp);
            }

            // Allow role updates - users can change their role anytime
            String selectedRole = body.get("role");

            // Validate role is one of the allowed options
            try {
                User.Role role = User.Role.valueOf(selectedRole.toUpperCase());
                // Only allow IMPORTER, EXPORTER, EXCHANGER
                if (role != User.Role.IMPORTER && role != User.Role.EXPORTER && role != User.Role.EXCHANGER) {
                    resp.put("success", false);
                    resp.put("message", "Invalid role. Only IMPORTER, EXPORTER, or EXCHANGER allowed");
                    return ResponseEntity.badRequest().body(resp);
                }

                user.setRole(role);
                user.setRoleSelected(true);
                userRepository.save(user);

                resp.put("success", true);
                resp.put("message", "Role selected successfully");
                resp.put("role", role.toString());
                return ResponseEntity.ok(resp);
            } catch (IllegalArgumentException e) {
                resp.put("success", false);
                resp.put("message", "Invalid role");
                return ResponseEntity.badRequest().body(resp);
            }
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Error selecting role: " + e.getMessage());
            return ResponseEntity.internalServerError().body(resp);
        }
    }

    // GET /api/user/profile
    // Returns current user profile
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        Map<String, Object> resp = new HashMap<>();
        String userEmail = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }

        User user = userOpt.get();
        user.setPassword(null); // Don't expose password

        resp.put("success", true);
        resp.put("user", user);
        return ResponseEntity.ok(resp);
    }
}
