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
            card.setOwnerName(cardDetails.getOwnerName());
            card.setBalance(cardDetails.getBalance());
            card.setActive(cardDetails.isActive());
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