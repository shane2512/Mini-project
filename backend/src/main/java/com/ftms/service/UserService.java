package com.ftms.service;

// UserService handles all business logic related to users.
// Business logic means: rules like "user must be APPROVED before placing orders",
// "password must be BCrypt encoded", "email must be unique" etc.
// The controller calls service, service calls repository.

import com.ftms.dto.RegisterRequest;
import com.ftms.model.User;
import com.ftms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok: auto-generates constructor for all final fields (dependency
                         // injection)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // BCrypt encoder from SecurityConfig

    // Registers a new user. Checks email uniqueness, encodes password, sets KYC as
    // PENDING.
    // Role is NOT selected during registration - user selects it after KYC
    // approval.
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt hash
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setBankName(request.getBankName());
        user.setAccountNumber(request.getAccountNumber());
        user.setSwiftCode(request.getSwiftCode());
        user.setIfscCode(request.getIfscCode());
        user.setPassportData(request.getPassportBase64()); // Base64 encoded image
        user.setRole(User.Role.IMPORTER); // Default role - user will select actual role after KYC approval
        user.setRoleSelected(false); // Explicitly mark that role hasn't been selected yet
        user.setKycStatus(User.KycStatus.PENDING); // always starts as PENDING

        return userRepository.save(user);
    }

    // Returns all users waiting for KYC approval - used by Admin dashboard
    public List<User> getPendingKycUsers() {
        return userRepository.findByKycStatus(User.KycStatus.PENDING);
    }

    // Admin approves a user's KYC - user can now login and use the system
    public User approveKyc(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setKycStatus(User.KycStatus.APPROVED);
        return userRepository.save(user);
    }

    // Admin rejects a user's KYC
    public User rejectKyc(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setKycStatus(User.KycStatus.REJECTED);
        return userRepository.save(user);
    }

    // Find user by email - used during login
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users - for admin dashboard
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
