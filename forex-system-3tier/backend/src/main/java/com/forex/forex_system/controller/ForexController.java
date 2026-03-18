package com.forex.forex_system.controller;
import com.forex.forex_system.model.ForexTransaction;
import com.forex.forex_system.service.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class ForexController {
    @Autowired private ForexService forexService;
    @GetMapping("/forex") public String forexPage() { return "forex"; }
    @GetMapping("/api/forex") @ResponseBody public List<ForexTransaction> getAll() { return forexService.getAllTransactions(); }
    @PostMapping("/api/forex") @ResponseBody public ForexTransaction create(@RequestBody ForexTransaction tx) { return forexService.performTransaction(tx); }
}

