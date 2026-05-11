package com.few_payments.transaction_service.blockchain;

import com.few_payments.transaction_service.TestcontainersConfiguration;
import com.few_payments.transaction_service.transaction.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * BlockchainIntegrationTest
 * Author: Edoardo Sabatini
 * Date: 2026-05-11
 * Company: few-payments
 * Description: Real integration test with Geth node via Web3j.
 */
@SpringBootTest()
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Import(TestcontainersConfiguration.class) //  It uses your working Docker config
@Slf4j // Lombok annotation to enable logging with log.info, log.error, etc.
class BlockchainIntegrationTest {

    private final BlockchainService blockchainService;

    @Test
    @DisplayName("Should connect to Geth and retrieve Master Wallet balance")
    void shouldRetrieveRealBalanceFromGeth() throws Exception {
        // Chiamata al metodo che hai scritto nel BlockchainService
        BigDecimal balance = blockchainService.getBalance();

        // It uses log.info to print the balance in a readable format instead of System.out.println, 
        // following the project's logging conventions.
        log.info("******************************************");
        log.info("💰 MASTER WALLET BALANCE ($FEW): {}", balance);
        log.info("******************************************");

        // Verify that we got a non-null balance and that it's a reasonable value (e.g., >= 0)
        assertNotNull(balance, "Balance should not be null");
        // We verify that the balance is at least 0 (or greater if you have already mined/pre-loaded funds)
        assertTrue(balance.compareTo(BigDecimal.ZERO) >= 0);
    }
}
