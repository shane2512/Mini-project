package com.ftms.repository;

// Repository interfaces handle all database operations.
// Spring Data JPA auto-generates the SQL query based on the method name.
// You never write SQL here. You just define the method signature.
// Example: findByEmail automatically becomes SELECT * FROM users WHERE email = ?

import com.ftms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring generates: SELECT * FROM users WHERE account_status = ?
    List<User> findByAccountStatus(User.AccountStatus accountStatus);

    // Spring generates: SELECT * FROM users WHERE kyc_status = ?
    List<User> findByKycStatus(User.KycStatus kycStatus);

    // Spring generates: SELECT * FROM users WHERE role = ?
    List<User> findByRole(User.Role role);

    // Spring generates: SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END
    // FROM users WHERE email = ?
    boolean existsByEmail(String email);
}
