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
        upsertDefaultUser("System Admin", "admin@ftms.com", User.Role.ADMIN, "+91-1111111111", "Admin Bank",
                "1000000001", "ADMNINBB", "ADMIN0001");
        upsertDefaultUser("Central Bank Officer", "central@ftms.com", User.Role.CENTRAL_BANK, "+91-2222222222",
                "Central Bank", "2000000002", "CNTRLNBB", "CNTRL0002");
        upsertDefaultUser("Commercial Bank Officer", "bank@ftms.com", User.Role.COMMERCIAL_BANK, "+91-3333333333",
                "Commercial Bank", "3000000003", "COMMNBB", "COMM0003");
        upsertDefaultUser("Importer Demo", "importer@ftms.com", User.Role.IMPORTER, "+91-4444444444", "HDFC Bank",
                "4000000004", "HDFCINBB", "HDFC0004");
        upsertDefaultUser("Exporter Demo", "exporter@ftms.com", User.Role.EXPORTER, "+91-5555555555", "ICICI Bank",
                "5000000005", "ICICIIN", "ICICI0005");
        upsertDefaultUser("Exchanger Demo", "exchanger@ftms.com", User.Role.EXCHANGER, "+91-6666666666", "Axis Bank",
                "6000000006", "AXISINBB", "AXIS0006");
    }

    private void upsertDefaultUser(String fullName, String email, User.Role role, String phone, String bankName,
            String accountNumber, String swiftCode, String ifscCode) {
        // Check if user exists, otherwise create new
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setPhone(phone);
        user.setBankName(bankName);
        user.setAccountNumber(accountNumber);
        user.setSwiftCode(swiftCode);
        user.setIfscCode(ifscCode);
        user.setKycStatus(User.KycStatus.APPROVED);
        user.setAccountStatus(User.AccountStatus.APPROVED);
        user.setRoleSelected(true);

        if (user.getPassword() == null || !passwordEncoder.matches(DEFAULT_PASSWORD, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        }

        userRepository.save(user);
    }
}
