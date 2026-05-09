package com.few_payments.transaction_service.transaction.service;

import com.few_payments.transaction_service.transaction.entity.Transaction;
import com.few_payments.transaction_service.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TransactionService
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Business layer for transaction queries.
 *              readOnly=true signals to the DB driver that no write lock is needed.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Returns all rows from the transactions table — equivalent to:
     * SELECT * FROM transactions;
     */
    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        log.info("Fetching all transactions from DB");
        List<Transaction> result = transactionRepository.findAll();
        log.info("Found {} transaction(s)", result.size());
        return result;
    }
}
