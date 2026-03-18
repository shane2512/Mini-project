package com.forex.forex_system.controller;
import com.forex.forex_system.model.ExchangeRate;
import com.forex.forex_system.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class ExchangeRateController {
    @Autowired private ExchangeRateService rateService;
    @GetMapping("/exchange-rates") public String ratesPage() { return "exchange-rates"; }
    @GetMapping("/api/exchange-rates") @ResponseBody public List<ExchangeRate> getAll() { return rateService.getAll(); }
    @PostMapping("/api/exchange-rates") @ResponseBody public ExchangeRate create(@RequestBody ExchangeRate rate) { return rateService.save(rate); }
}

