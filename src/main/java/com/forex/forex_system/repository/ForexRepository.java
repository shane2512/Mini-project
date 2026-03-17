package com.forex.forex_system.repository;
import com.forex.forex_system.model.ForexTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ForexRepository extends JpaRepository<ForexTransaction, Long> {
    List<ForexTransaction> findByCustomerId(Long customerId);
}

