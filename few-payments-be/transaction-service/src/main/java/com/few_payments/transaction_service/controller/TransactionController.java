package com.few_payments.transaction_service.controller;

import com.few_payments.transaction_service.transaction.entity.Transaction;
import com.few_payments.transaction_service.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TransactionController
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: REST endpoint for transaction queries.
 *              GET /api/v1/transactions → SELECT * FROM transactions
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * GET /api/v1/transactions
     * Returns all transactions as JSON.
     * Equivalent to the psql command in postgres_test_connection.sh.
     *
     * @return 200 OK with list of transactions (empty array [] if table is empty)
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.findAll());
    }
}
