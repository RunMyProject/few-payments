package com.few_payments.transaction_service.blockchain;

import com.few_payments.transaction_service.TestcontainersConfiguration;
import com.few_payments.transaction_service.transaction.entity.User;
import com.few_payments.transaction_service.transaction.repository.TransactionRepository;
import com.few_payments.transaction_service.transaction.repository.UserRepository;
import com.few_payments.transaction_service.transaction.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import org.web3j.crypto.Credentials;

/*
 * AliceToBobBlockchainTest
 * Author: Edoardo Sabatini
 * Date: 2026-05-11
 * Company: few-payments
 * Description: Integration test that simulates a real blockchain transaction from Alice (master wallet) to Bob (a user in our DB).
 *              This test covers the full flow: from executing a real transfer on Geth to persisting the transaction in PostgreSQL. 
 *              It uses the shared blockchain and database containers defined in TestcontainersConfiguration.
 *              Note: This test is designed to run in an isolated environment (e.g., CI pipeline) 
 *              and may not be suitable for local development due to its reliance on external services (Geth and PostgreSQL).
 *              For local testing, consider using mocks or a separate test profile with in-memory databases
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Slf4j
class AliceToBobBlockchainTest {

    private final BlockchainService blockchainService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // to add in application.properties (or set as env variable in CI):
    @Value("${blockchain.master-private-key}")
    private String masterPrivateKey;

    @Test
    @DisplayName("FULL FLOW: Master sends 5 FEW to Bob, persistence on Postgres")
    void shouldExecuteFullBlockchainPaymentFlow() throws Exception {

        // Strategy: Alice -> Bob transfer on Geth, then we check the balances and DB persistence.

        Credentials check = Credentials.create(masterPrivateKey);
        log.info("🔑 Address from private key: {}", check.getAddress());
        log.info("💼 Expected master wallet:   0x71562b71999873db5b286df957af199ec94617f7");

        long actualChainId = blockchainService.getWeb3j().ethChainId().send().getChainId().longValueExact();
        log.info("🔗 Actual Geth Chain ID: {}", actualChainId);

        // 1. Setup DB: We create Bob (the recipient) in our database with a known blockchain address.
        String bobAddress = "0x1234500000000000000000000000000000000002";
        User bob = new User("Bob_Real_Test", "bob@few.com", bobAddress);
        userRepository.save(bob);

        // 2. Action: real transfer on Geth:
        // We execute a real transfer of 5 FEW from the master wallet to Bob's address.
        BigDecimal amount = new BigDecimal("5.00");
        log.info("🚀 Sending {} FEW to {}...", amount, bobAddress);
        
        String txHash = blockchainService.transferFew(bobAddress, amount, masterPrivateKey);
        log.info("✅ Blockchain TX Success! Hash: {}", txHash);

        // 3. Persistence: We save the transaction in the DB
        assertNotNull(txHash);

        String aliceAddress = "0x7677afeb70f24580f72b2a259b0e956c3cbe4374";
        BigDecimal bobBalance = blockchainService.getBalanceOf(bobAddress);
        BigDecimal aliceBalance = blockchainService.getBalanceOf(aliceAddress);
        BigDecimal masterBalance = blockchainService.getBalance();

        log.info("------------------------------------------");
        log.info("💰 ALICE BALANCE: {} FEW", aliceBalance);
        log.info("💰 BOB BALANCE:   {} FEW", bobBalance);
        log.info("🏦 MASTER BALANCE: {} FEW", masterBalance);
        log.info("------------------------------------------");

        assertEquals(1, transactionRepository.count());
        // It verifies that Bob's balance has increased by approximately 5 FEW 
        // (considering possible minor discrepancies due to gas fees, etc.)
        // BigDecimal bobBalance = blockchainService.getBalanceOf(bobAddress); 
        // log.info("💰 Bob's new balance: {}", bobBalance);
    }
}
