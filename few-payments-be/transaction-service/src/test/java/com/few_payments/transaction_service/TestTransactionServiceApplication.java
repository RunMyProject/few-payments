package com.few_payments.transaction_service;

import org.springframework.boot.SpringApplication;

/**
 * TestTransactionServiceApplication
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Alternative entry point for local development without a real PostgreSQL instance.
 *              Boots the full application replacing the datasource with a Testcontainers container.
 *              Run this main() instead of TransactionServiceApplication when you want to develop
 *              without Docker Compose or the real DB running.
 *
 * Usage: run this class from your IDE as a standard Java application.
 */
public class TestTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(TransactionServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}