package com.forex.forex_system.controller;
import com.forex.forex_system.model.Currency;
import com.forex.forex_system.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class CurrencyController {
    @Autowired private CurrencyService currencyService;
    @GetMapping("/currencies") public String currenciesPage() { return "currencies"; }
    @GetMapping("/api/currencies") @ResponseBody public List<Currency> getAll() { return currencyService.getAll(); }
    @PostMapping("/api/currencies") @ResponseBody public Currency create(@RequestBody Currency currency) { return currencyService.save(currency); }
    @DeleteMapping("/api/currencies/{code}") @ResponseBody public ResponseEntity<?> delete(@PathVariable String code) { currencyService.delete(code); return ResponseEntity.ok().build(); }
}

