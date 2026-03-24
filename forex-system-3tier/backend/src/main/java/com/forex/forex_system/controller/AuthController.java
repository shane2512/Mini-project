package com.forex.forex_system.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AuthController {
    @GetMapping("/") public String login() { return "redirect:/healthz"; }
    @GetMapping("/register") public String register() { return "redirect:/healthz"; }
    @GetMapping("/dashboard") public String dashboard() { return "redirect:/healthz"; }
}

