package com.forex.forex_system.controller;
import com.forex.forex_system.model.Bank;
import com.forex.forex_system.model.CountryBank;
import com.forex.forex_system.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class BankController {
    @Autowired private BankService bankService;
    @GetMapping("/banks") public String banksPage() { return "banks"; }
    @GetMapping("/api/banks") @ResponseBody public List<Bank> getAll() { return bankService.getAllBanks(); }
    @PostMapping("/api/banks") @ResponseBody public Bank create(@RequestBody Bank bank) { return bankService.saveBank(bank); }
    @GetMapping("/api/central-banks") @ResponseBody public List<CountryBank> getCentralBanks() { return bankService.getAllCentralBanks(); }
    @PostMapping("/api/central-banks") @ResponseBody public CountryBank createCentral(@RequestBody CountryBank cb) { return bankService.saveCentralBank(cb); }
}

