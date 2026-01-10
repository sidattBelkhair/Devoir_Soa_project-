package com.example.fraudservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    @GetMapping("/check")
    // On renvoie un String simple contenant du JSON
    public String checkFraud(@RequestParam double amount) {
        
        boolean isFraudulent = false;
        String reason = "Transaction OK";

        if (amount > 10000) {
            isFraudulent = true;
            reason = "Montant trop élevé";
        }

        // On construit le JSON manuellement pour être sûr qu'il n'est pas vide
        return "{ \"isFraudulent\": " + isFraudulent + ", \"reason\": \"" + reason + "\" }";
    }
}