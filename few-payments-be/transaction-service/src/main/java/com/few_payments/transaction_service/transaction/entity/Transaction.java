package com.few_payments.transaction_service.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: JPA entity mirroring the 'transactions' table in schema.sql.
 *              Uses BigDecimal for NUMERIC(38,18) — never use float for money.
 */
@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@ToString
public class Transaction {

    /** Unique record identifier — mapped to UUID PRIMARY KEY in schema.sql */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Blockchain tx hash — UNIQUE NOT NULL in schema.sql */
    @Column(name = "transaction_hash", unique = true, nullable = false)
    private String transactionHash;

    /** Sender wallet address */
    @Column(name = "sender_wallet", nullable = false)
    private String senderWallet;

    /** Receiver wallet address */
    @Column(name = "receiver_wallet", nullable = false)
    private String receiverWallet;

    /**
     * Token amount — NUMERIC(38,18) maps to BigDecimal.
     * precision=38, scale=18 mirrors the 18 decimal places of ERC-20 tokens.
     */
    @Column(name = "amount", nullable = false, precision = 38, scale = 18)
    private BigDecimal amount;

    /** SAGA lifecycle state: PENDING | CONFIRMED | FAILED | COMPENSATED */
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    /** Correlation ID for distributed SAGA orchestration */
    @Column(name = "saga_id")
    private UUID sagaId;

    /** Immutable audit timestamp — DEFAULT CURRENT_TIMESTAMP in schema.sql */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}