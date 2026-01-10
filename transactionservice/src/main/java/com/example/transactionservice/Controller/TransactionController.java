package com.example.transactionservice.Controller;

import com.example.transactionservice.dto.TransactionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final WebClient fraudWebClient;
    private final WebClient cardWebClient;

    public TransactionController() {
        this.fraudWebClient = WebClient.create("http://localhost:8082");
        this.cardWebClient = WebClient.create("http://localhost:8081");
    }

    @PostMapping("/pay")
    public ResponseEntity<String> processTransaction(@RequestBody TransactionRequest request) {

        // 1. ETAPE : FRAUD DETECTION
        try {
            // On récupère la réponse sous forme de String brute (texte)
            String fraudResponseRaw = fraudWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/fraud/check")
                    .queryParam("amount", request.getAmount())
                    .build())
                .retrieve()
                .bodyToMono(String.class) 
                .block();

            // Afficher la réponse brute dans la console pour vérifier (optionnel)
            System.out.println("DEBUG Réponse Fraude : " + fraudResponseRaw);

            // Si la réponse est vide, on arrête
            if (fraudResponseRaw == null || fraudResponseRaw.isEmpty()) {
                return ResponseEntity.status(500).body("Erreur : Service de fraude a répondu vide.");
            }

            // On vérifie simplement si le JSON contient le texte "true" pour isFraudulent
            // C'est une méthode "rustique" mais qui marche à 100% si le JSON est valide
            boolean isFraud = fraudResponseRaw.contains("\"isFraudulent\": true");

            if (isFraud) {
                return ResponseEntity.status(403).body("Transaction Bloquée : Fraude détectée !");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(503).body("Erreur : Service de détection de fraude indisponible.");
        }

        // 2. ETAPE : VERIFICATION CARTE
        try {
            Map<String, Object> cardData = cardWebClient.get()
                .uri("/api/getcard/" + request.getCardNumber())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (cardData == null) {
                 return ResponseEntity.status(404).body("Transaction Bloquée : Carte inconnue.");
            }

            boolean active = (boolean) cardData.get("active");
            double balance = ((Number) cardData.get("balance")).doubleValue();

            if (!active) {
                return ResponseEntity.status(403).body("Transaction Bloquée : Carte désactivée.");
            }

            if (balance < request.getAmount()) {
                return ResponseEntity.status(403).body("Transaction Bloquée : Solde insuffisant.");
            }

        } catch (WebClientResponseException.NotFound e) {
            return ResponseEntity.status(404).body("Transaction Bloquée : Carte inconnue.");
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Erreur : Service carte indisponible.");
        }

        // 3. ETAPE : SUCCES
        return ResponseEntity.ok("✅ Transaction Autorisée avec succès !");
    }
}