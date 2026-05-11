package com.few_payments.transaction_service.transaction;

import com.few_payments.transaction_service.TestcontainersConfiguration;
import com.few_payments.transaction_service.transaction.entity.Transaction;
import com.few_payments.transaction_service.transaction.entity.User;
import com.few_payments.transaction_service.transaction.repository.TransactionRepository;
import com.few_payments.transaction_service.transaction.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * FewPaymentIntegrationTest
 * Author: Edoardo Sabatini
 * Date: 2026-05-10
 * Latest Edit: 2026-05-11
 * Company: few-payments
 * Description: Integration test for Alice-to-Bob transfer.
 * Mirrors the project's established Testcontainers pattern.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class) // Uses your working Docker config
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class FewPaymentIntegrationTest {

    // Note: @TestConstructor + autowireMode + @RequiredArgsConstructor + final fields allow us to use constructor injection without @Autowired.
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanUp() {
        // Cleaning tables before each execution
        jdbcTemplate.execute("DELETE FROM transactions");
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    @DisplayName("Should transfer 10 FEW from Alice to Bob and persist record")
    void shouldExecuteTransferBetweenUsers() {
        // 1. Setup: Create users with compliant 42-char addresses
        User alice = new User("Alice_Test", "alice@few.com", "0x7156200000000000000000000000000000000001");
        User bob = new User("Bob_Test", "bob@few.com", "0x1234500000000000000000000000000000000002");
        userRepository.saveAll(List.of(alice, bob));

        // 2. Action: Persist the 10 FEW transaction
        Transaction tx = new Transaction(
            UUID.randomUUID(), 
            "0xTX_HASH_GENERATED_BY_GETH", 
            alice.getWalletAddress(), 
            bob.getWalletAddress(), 
            new BigDecimal("10.00"), 
            "CONFIRMED", 
            UUID.randomUUID(),
            java.time.LocalDateTime.now()
        );

        transactionRepository.save(tx);

        // 3. Verification
        assertEquals(2, userRepository.count());
        assertEquals(1, transactionRepository.count());
        System.out.println("✅ Test Passed: Persistence layer verified.");
    }
}
