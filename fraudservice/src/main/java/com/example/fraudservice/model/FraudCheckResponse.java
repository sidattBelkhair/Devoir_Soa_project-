package com.example.fraudservice.model;

// J'ai enlevé Lombok Data pour éviter le bug du JSON vide
public class FraudCheckResponse {
    private boolean isFraudulent;
    private String reason;

    // Constructeur manuel (OBLIGATOIRE)
    public FraudCheckResponse(boolean isFraudulent, String reason) {
        this.isFraudulent = isFraudulent;
        this.reason = reason;
    }

    // Getters manuels (OBLIGATOIRES pour le JSON)
    public boolean isFraudulent() { return isFraudulent; }
    public String getReason() { return reason; }
    
    // Setters
    public void setFraudulent(boolean fraudulent) { this.isFraudulent = fraudulent; }
    public void setReason(String reason) { this.reason = reason; }
}