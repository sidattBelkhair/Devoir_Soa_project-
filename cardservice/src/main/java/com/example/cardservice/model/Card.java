package com.example.cardservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identité carte
    @Column(unique = true, nullable = false, length = 19)
    private String cardNumber;              // ex: 4111111111111111 (16-19)

    @Column(nullable = false)
    private String ownerName;               // Nom propriétaire (titulaire)

    @Column(nullable = false, length = 10)
    private String cardType;                // VISA / MASTERCARD

    // Sécurité / validité
    @Column(nullable = false, length = 4)
    private String cvv;                     // CVC/CVV (3 ou 4 chiffres) - projet

    @Column(nullable = false)
    private LocalDate expirationDate;       // Date expiration (YYYY-MM-DD)

    @Column(nullable = false)
    private boolean active;                 // active ou bloquée

    @Column(nullable = false)
    private boolean blocked;                // blocage manuel (fraude, perte...)

    // Solde / limites
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;             // Solde disponible

      // Limite mensuelle (optionnel)

    @Column(nullable = false, length = 3)
    private String currency;                // "MRU", "EUR", "USD"

  
    private String status;                  // ACTIVE / EXPIRED / BLOCKED (texte)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Auto set dates
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "ACTIVE";
        if (currency == null) currency = "MRU";
        if (balance == null) balance = BigDecimal.ZERO;
       
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
