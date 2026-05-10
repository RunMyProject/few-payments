package com.few_payments.transaction_service.transaction.repository;

import com.few_payments.transaction_service.transaction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository
 * Author: Edoardo Sabatini
 * Date: 2026-05-10
 * Description: Repository for managing user-wallet mapping.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Utile per recuperare l'utente dal suo indirizzo blockchain
    Optional<User> findByWalletAddress(String walletAddress);
}
