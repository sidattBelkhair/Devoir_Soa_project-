package com.example.transactionservice.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String cardNumber;
    private double amount;
}