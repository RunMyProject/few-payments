package com.few_payments.transaction_service.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * User
 * Author: Edoardo Sabatini
 * Date: 2026-05-10
 * Description: Maps human identities to blockchain addresses.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "wallet_address", unique = true, nullable = false, length = 42)
    private String walletAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Comodo costruttore rapido per i test
    public User(String username, String email, String walletAddress) {
        this.username = username;
        this.email = email;
        this.walletAddress = walletAddress;
        this.createdAt = LocalDateTime.now();
    }
}
