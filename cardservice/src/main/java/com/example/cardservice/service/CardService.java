package com.example.cardservice.service;

import com.example.cardservice.model.Card;
import com.example.cardservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    // Créer une carte
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    // Récupérer une carte par son numéro (Unique)
    public Optional<Card> getCardByNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    // Récupérer toutes les cartes
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Mettre à jour une carte
    public Card updateCard(String cardNumber, Card cardDetails) {
    return cardRepository.findByCardNumber(cardNumber).map(card -> {

        // Mise à jour PARTIELLE : on modifie seulement si la valeur est fournie

        if (cardDetails.getOwnerName() != null) {
            card.setOwnerName(cardDetails.getOwnerName());
        }

        if (cardDetails.getCardType() != null) {
            card.setCardType(cardDetails.getCardType());
        }

        if (cardDetails.getCvv() != null) {
            card.setCvv(cardDetails.getCvv());
        }

        if (cardDetails.getExpirationDate() != null) {
            card.setExpirationDate(cardDetails.getExpirationDate());
        }

        // Boolean: comme c'est toujours true/false, on accepte la valeur telle quelle
        // (donc le client doit envoyer active/blocked s'il veut changer)
        card.setActive(cardDetails.isActive());
        card.setBlocked(cardDetails.isBlocked());

        if (cardDetails.getBalance() != null) {
            card.setBalance(cardDetails.getBalance());
        }

        if (cardDetails.getCurrency() != null) {
            card.setCurrency(cardDetails.getCurrency());
        }

        if (cardDetails.getStatus() != null) {
            card.setStatus(cardDetails.getStatus());
        }

        return cardRepository.save(card);

    }).orElse(null);
}


    // Supprimer une carte
    public boolean deleteCard(String cardNumber) {
        if (cardRepository.findByCardNumber(cardNumber).isPresent()) {
            cardRepository.deleteByCardNumber(cardNumber);
            return true;
        }
        return false;
    }
}