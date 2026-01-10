package com.example.cardservice.repository;

import com.example.cardservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
    
    // Ajoute ceci pour la suppression
    @Transactional
    void deleteByCardNumber(String cardNumber);
}