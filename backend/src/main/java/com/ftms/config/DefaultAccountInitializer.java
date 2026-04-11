package com.ftms.config;

import com.ftms.model.User;
import com.ftms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAccountInitializer implements CommandLineRunner {

    private static final String DEFAULT_PASSWORD = "Admin@123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        upsertDefaultUser("System Admin", "admin@ftms.com", User.Role.ADMIN);
        upsertDefaultUser("Central Bank Officer", "centralbank@ftms.com", User.Role.CENTRAL_BANK);
        upsertDefaultUser("Commercial Bank Officer", "bank@ftms.com", User.Role.COMMERCIAL_BANK);
        upsertDefaultUser("Importer Demo", "importer@ftms.com", User.Role.IMPORTER);
        upsertDefaultUser("Exporter Demo", "exporter@ftms.com", User.Role.EXPORTER);
        upsertDefaultUser("Exchanger Demo", "exchanger@ftms.com", User.Role.EXCHANGER);
    }

    private void upsertDefaultUser(String fullName, String email, User.Role role) {
        User user = userRepository.findByEmail(email).orElseGet(User::new);

        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setKycStatus(User.KycStatus.APPROVED);
        user.setRoleSelected(true);

        if (user.getPassword() == null || !passwordEncoder.matches(DEFAULT_PASSWORD, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        }

        userRepository.save(user);
    }
}
