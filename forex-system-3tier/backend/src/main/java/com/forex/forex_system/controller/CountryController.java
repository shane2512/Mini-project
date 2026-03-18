package com.forex.forex_system.controller;
import com.forex.forex_system.model.Country;
import com.forex.forex_system.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class CountryController {
    @Autowired private CountryService countryService;
    @GetMapping("/countries") public String countriesPage() { return "countries"; }
    @GetMapping("/api/countries") @ResponseBody public List<Country> getAll() { return countryService.getAll(); }
    @PostMapping("/api/countries") @ResponseBody public Country create(@RequestBody Country country) { return countryService.save(country); }
    @DeleteMapping("/api/countries/{id}") @ResponseBody public ResponseEntity<?> delete(@PathVariable Long id) { countryService.delete(id); return ResponseEntity.ok().build(); }
}

