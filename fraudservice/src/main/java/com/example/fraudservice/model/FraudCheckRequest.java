package com.example.fraudservice.model;

public class FraudCheckRequest {
    private String cardNumber;     // optionnel mais utile
    private double amount;
    private String currency;       // "MRU", "EUR", ...
    private String merchantId;     // identifiant commerçant
    private String merchantCategory; // MCC ou catégorie
    private String country;        // pays du paiement: "MR"
    private String channel;        // POS / ECOM / ATM

    public FraudCheckRequest() {}

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
}
