package com.example.fraudservice.controller;

import com.example.fraudservice.model.FraudCheckRequest;
import com.example.fraudservice.model.FraudCheckResponse;
import com.example.fraudservice.service.FraudScoringService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudScoringService scoringService;

    public FraudController(FraudScoringService scoringService) {
        this.scoringService = scoringService;
    }

    // ✅ vrai style: POST avec JSON
    @PostMapping("/check")
    public FraudCheckResponse checkFraud(@RequestBody FraudCheckRequest request) {
        return scoringService.evaluate(request);
    }

    // (optionnel) pour compatibilité: GET simple
    @GetMapping("/check")
    public FraudCheckResponse checkFraudSimple(@RequestParam double amount) {
        FraudCheckRequest req = new FraudCheckRequest();
        req.setAmount(amount);
        req.setChannel("POS");
        req.setCountry("MR");
        req.setCurrency("MRU");
        req.setMerchantId("DEFAULT");
        req.setMerchantCategory("GENERAL");
        return scoringService.evaluate(req);
    }
}
