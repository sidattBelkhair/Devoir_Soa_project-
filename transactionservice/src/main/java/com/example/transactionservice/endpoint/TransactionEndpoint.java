package com.example.transactionservice.endpoint;

import com.example.transactionservice.generated.TransactionRequest;
import com.example.transactionservice.generated.TransactionResponse;
import com.example.transactionservice.model.TransactionLog;
import com.example.transactionservice.service.TransactionLogService;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.util.Map;

@Endpoint
public class TransactionEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/transactionservice";

    private final WebClient fraudWebClient = WebClient.create("http://localhost:8082");
    private final WebClient cardWebClient  = WebClient.create("http://localhost:8081");
    private final TransactionLogService logService;

    // ✅ injection Spring
    public TransactionEndpoint(TransactionLogService logService) {
        this.logService = logService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TransactionRequest")
    @ResponsePayload
    public TransactionResponse processTransaction(@RequestPayload TransactionRequest request) {

        TransactionResponse response = new TransactionResponse();
        String cardNumber = request.getCardNumber() == null ? "" : request.getCardNumber().trim();
        double amount = request.getAmount();

        // 1) FRAUD CHECK
        try {
            String fraudResponseRaw = fraudWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/fraud/check")
                            .queryParam("amount", amount)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (fraudResponseRaw == null || fraudResponseRaw.contains("\"isFraudulent\":true")) {
                response.setStatus("BLOCKED");
                response.setMessage("Fraude détectée !");
                logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
                return response;
            }
        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("Service Fraude Indisponible");
            logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
            return response;
        }

        // 2) CARD CHECK
        try {
            Map<String, Object> cardData = cardWebClient.get()
                    .uri("/api/getcard/" + cardNumber)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (cardData == null) {
                response.setStatus("NOT_FOUND");
                response.setMessage("Carte inconnue");
                logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
                return response;
            }

            String expStr = (String) cardData.get("expirationDate");
            if (expStr != null) {
                LocalDate exp = LocalDate.parse(expStr);
                if (exp.isBefore(LocalDate.now())) {
                    response.setStatus("BLOCKED");
                    response.setMessage("Carte expirée");
                    logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
                    return response;
                }
            }

            boolean active = Boolean.TRUE.equals(cardData.get("active"));
            double balance = Double.parseDouble(cardData.get("balance").toString());

            if (!active) {
                response.setStatus("BLOCKED");
                response.setMessage("Carte désactivée");
                logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
                return response;
            }

            if (balance < amount) {
                response.setStatus("BLOCKED");
                response.setMessage("Solde insuffisant");
                logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
                return response;
            }

        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("Service Carte Indisponible");
            logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
            return response;
        }

        // 3) SUCCESS
        response.setStatus("SUCCESS");
        response.setMessage("Transaction Autorisée");
        logService.add(new TransactionLog(cardNumber, amount, response.getStatus(), response.getMessage()));
        return response;
    }
}
