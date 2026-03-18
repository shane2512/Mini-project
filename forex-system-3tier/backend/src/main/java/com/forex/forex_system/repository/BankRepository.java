package com.forex.forex_system.repository;
import com.forex.forex_system.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {}

