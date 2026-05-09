package com.few_payments.transaction_service.transaction.repository;

import com.few_payments.transaction_service.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * TransactionRepository
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Spring Data repository for the transactions table.
 *              findAll() is all we need for the initial SELECT *.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    // findAll() and findById() are provided by JpaRepository — zero boilerplate.
}
