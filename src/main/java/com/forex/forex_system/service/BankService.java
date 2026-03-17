package com.forex.forex_system.service;
import com.forex.forex_system.model.Bank;
import com.forex.forex_system.model.CountryBank;
import com.forex.forex_system.repository.BankRepository;
import com.forex.forex_system.repository.CountryBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class BankService {
    @Autowired private BankRepository bankRepo;
    @Autowired private CountryBankRepository countryBankRepo;
    public List<Bank> getAllBanks() { return bankRepo.findAll(); }
    public Bank saveBank(Bank b) { return bankRepo.save(b); }
    public void deleteBank(Long id) { bankRepo.deleteById(id); }
    public List<CountryBank> getAllCentralBanks() { return countryBankRepo.findAll(); }
    public CountryBank saveCentralBank(CountryBank cb) { return countryBankRepo.save(cb); }
}

