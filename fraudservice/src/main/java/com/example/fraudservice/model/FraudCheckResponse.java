package com.example.fraudservice.model;

import java.util.List;

public class FraudCheckResponse {
    private boolean fraudulent;
    private int riskScore;              // 0..100
    private String decision;            // APPROVE / REVIEW / DECLINE
    private String reason;              // message simple
    private List<String> triggeredRules; // règles déclenchées

    public FraudCheckResponse() {}

    public FraudCheckResponse(boolean fraudulent, int riskScore, String decision, String reason, List<String> triggeredRules) {
        this.fraudulent = fraudulent;
        this.riskScore = riskScore;
        this.decision = decision;
        this.reason = reason;
        this.triggeredRules = triggeredRules;
    }

    public boolean isFraudulent() { return fraudulent; }
    public void setFraudulent(boolean fraudulent) { this.fraudulent = fraudulent; }

    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public List<String> getTriggeredRules() { return triggeredRules; }
    public void setTriggeredRules(List<String> triggeredRules) { this.triggeredRules = triggeredRules; }
}
