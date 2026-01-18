package com.example.fraudservice.service;

import com.example.fraudservice.model.FraudCheckRequest;
import com.example.fraudservice.model.FraudCheckResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FraudScoringService {

    // Exemples "banque réaliste" (liste noire / MCC risqués)
    private static final Set<String> HIGH_RISK_MCC = Set.of("GAMBLING", "CRYPTO", "ADULT", "WIRE_TRANSFER");
    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of("XX", "YY"); // exemple (mets ce que tu veux)

    public FraudCheckResponse evaluate(FraudCheckRequest req) {
        List<String> rules = new ArrayList<>();
        int score = 0;

        // 1) Validation de base
        if (req.getAmount() <= 0) {
            rules.add("INVALID_AMOUNT");
            return decision(100, rules, "Montant invalide");
        }

        // 2) Montant (pas juste un seuil unique)
        if (req.getAmount() >= 50000) { score += 60; rules.add("VERY_HIGH_AMOUNT"); }
        else if (req.getAmount() >= 20000) { score += 40; rules.add("HIGH_AMOUNT"); }
        else if (req.getAmount() >= 10000) { score += 25; rules.add("ELEVATED_AMOUNT"); }
        else if (req.getAmount() >= 5000) { score += 10; rules.add("MEDIUM_AMOUNT"); }

        // 3) Channel (ECOM plus risqué que POS généralement)
        String channel = safe(req.getChannel());
        if (channel.equals("ECOM")) { score += 20; rules.add("ECOM_CHANNEL"); }
        if (channel.equals("ATM")) { score += 15; rules.add("ATM_CHANNEL"); }

        // 4) Pays
        String country = safe(req.getCountry());
        if (!country.isEmpty() && HIGH_RISK_COUNTRIES.contains(country)) {
            score += 35;
            rules.add("HIGH_RISK_COUNTRY");
        }

        // 5) Catégorie commerçant
        String mcc = safe(req.getMerchantCategory());
        if (!mcc.isEmpty() && HIGH_RISK_MCC.contains(mcc)) {
            score += 30;
            rules.add("HIGH_RISK_MERCHANT_CATEGORY");
        }

        // 6) Données manquantes (en banque, manque d’infos = risque)
        if (safe(req.getMerchantId()).isEmpty()) { score += 10; rules.add("MISSING_MERCHANT_ID"); }
        if (safe(req.getCurrency()).isEmpty()) { score += 5; rules.add("MISSING_CURRENCY"); }
        if (safe(req.getCardNumber()).isEmpty()) { score += 5; rules.add("MISSING_CARD_NUMBER"); }

        // Clamp score 0..100
        score = Math.max(0, Math.min(100, score));

        // Décision:
        // 0-29: APPROVE, 30-59: REVIEW, 60+: DECLINE
        return decision(score, rules, "Evaluation terminée");
    }

    private FraudCheckResponse decision(int score, List<String> rules, String msg) {
        String decision;
        boolean fraudulent;

        if (score >= 60) {
            decision = "DECLINE";
            fraudulent = true;
        } else if (score >= 30) {
            decision = "REVIEW";
            fraudulent = false; // pas “fraude certaine”, juste “suspect”
        } else {
            decision = "APPROVE";
            fraudulent = false;
        }

        String reason = msg + " | score=" + score + " | decision=" + decision;
        return new FraudCheckResponse(fraudulent, score, decision, reason, rules);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }
}
