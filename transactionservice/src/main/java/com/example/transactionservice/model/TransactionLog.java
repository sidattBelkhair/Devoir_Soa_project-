package com.example.transactionservice.model;

import java.time.LocalDateTime;

public class TransactionLog {
    private final String cardNumber;
    private final double amount;
    private final String status;   // SUCCESS / BLOCKED / ERROR / NOT_FOUND
    private final String message;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public TransactionLog(String cardNumber, double amount, String status, String message) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.status = status;
        this.message = message;
    }

    public String getCardNumber() { return cardNumber; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
