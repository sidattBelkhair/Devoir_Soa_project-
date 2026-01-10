package com.example.cardservice.controller;

import com.example.cardservice.model.Card;
import com.example.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    // 1. Créer (POST)
    @PostMapping("/addcard")
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card savedCard = cardService.createCard(card);
        return ResponseEntity.ok(savedCard);
    }

    // 2. Lire par ID (GET) - Pour les autres services
    @GetMapping("/getcard/{cardNumber}")
    public ResponseEntity<Card> getCard(@PathVariable String cardNumber) {
        return cardService.getCardByNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Lire tout (GET) - Pour voir toutes les cartes
    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    // 4. Mettre à jour (PUT)
    @PutMapping("/updatecard/{cardNumber}")
    public ResponseEntity<Card> updateCard(@PathVariable String cardNumber, @RequestBody Card cardDetails) {
        Card updatedCard = cardService.updateCard(cardNumber, cardDetails);
        if (updatedCard != null) {
            return ResponseEntity.ok(updatedCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Supprimer (DELETE)
    @DeleteMapping("/deletecard/{cardNumber}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        boolean isDeleted = cardService.deleteCard(cardNumber);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}