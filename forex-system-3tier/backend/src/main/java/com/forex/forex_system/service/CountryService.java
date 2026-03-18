package com.forex.forex_system.service;
import com.forex.forex_system.model.Country;
import com.forex.forex_system.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CountryService {
    @Autowired private CountryRepository repo;
    public List<Country> getAll() { return repo.findAll(); }
    public Country save(Country c) { return repo.save(c); }
    public void delete(Long id) { repo.deleteById(id); }
}

