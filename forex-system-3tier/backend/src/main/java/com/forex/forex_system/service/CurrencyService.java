package com.forex.forex_system.service;
import com.forex.forex_system.model.Currency;
import com.forex.forex_system.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CurrencyService {
    @Autowired private CurrencyRepository repo;
    public List<Currency> getAll() { return repo.findAll(); }
    public Currency save(Currency c) { return repo.save(c); }
    public void delete(String code) { repo.deleteById(code); }
}

