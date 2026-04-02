package com.ftms.service;

import com.ftms.dto.RegisterRequest;
import com.ftms.model.User;
import com.ftms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setBankName(request.getBankName());
        user.setAccountNumber(request.getAccountNumber());
        user.setSwiftCode(request.getSwiftCode());
        user.setIfscCode(request.getIfscCode());
        user.setRole(request.getRole());
        user.setKycStatus(User.KycStatus.PENDING);

        return userRepository.save(user);
    }

    public List<User> getPendingKycUsers() {
        return userRepository.findByKycStatus(User.KycStatus.PENDING);
    }

    public User approveKyc(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setKycStatus(User.KycStatus.APPROVED);
        return userRepository.save(user);
    }

    public User rejectKyc(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setKycStatus(User.KycStatus.REJECTED);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
