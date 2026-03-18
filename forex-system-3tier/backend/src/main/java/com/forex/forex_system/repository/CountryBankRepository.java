package com.forex.forex_system.repository;
import com.forex.forex_system.model.CountryBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CountryBankRepository extends JpaRepository<CountryBank, Long> {}

