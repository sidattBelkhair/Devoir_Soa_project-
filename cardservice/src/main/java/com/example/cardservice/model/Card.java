package com.example.cardservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // AJOUT IMPORTANT : unique = true empêche les doublons
    @Column(unique = true, nullable = false) 
    private String cardNumber; 
    
    private String ownerName;
    private double balance;
    
    private boolean active;
}