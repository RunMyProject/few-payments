package com.few_payments.transaction_service.blockchain;

import com.few_payments.transaction_service.TestcontainersConfiguration;
import com.few_payments.transaction_service.transaction.entity.User;
import com.few_payments.transaction_service.transaction.entity.Transaction;
import com.few_payments.transaction_service.transaction.repository.TransactionRepository;
import com.few_payments.transaction_service.transaction.repository.UserRepository;
import com.few_payments.transaction_service.transaction.service.FewTokenService;
import com.few_payments.transaction_service.contracts.FewToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.RawTransactionManager;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/*
 * AliceToBobFewTokenTest
 * Author: Edoardo Sabatini
 * Date: 2026-05-14
 * Company: few-payments
 * Description: Integration test for ERC20 FEW Token flow. 
 * It deploys the contract and tests transfers using the new FewTokenService.
 * Integration test that simulates a real ERC20 token transaction from Alice (master wallet) 
 * to Bob (a user in our DB) using the FewToken smart contract.
 * This test covers the full flow: from deploying the FewToken contract, 
 * executing a real transfer on Geth, to verifying the balance of Bob.
 * It uses the shared blockchain and database containers defined in TestcontainersConfiguration.
 * Note: This test is designed to run in an isolated environment 
 * (e.g., CI pipeline with Geth and PostgreSQL containers) and may not be suitable for local 
 * development due to its reliance on external services.
 * For local testing, consider using mocks or a separate test profile with in-memory databases.
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Slf4j
class AliceToBobFewTokenTest {

    private final FewTokenService fewTokenService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Test
    @DisplayName("ERC20 FLOW: Master sends 10 FEW to Bob, persistence on Postgres")
    void shouldExecuteFewTokenFlow() throws Exception {
        
        // 1. SETUP CREDENTIALS
        String masterKey = "b8c1b5c9331f005922a90231c39906665175a61d592202dfd00b5336d39999a4";
        Credentials credentials = Credentials.create(masterKey);
        
        // 2. CHAIN ID & MANAGER
        long chainId = fewTokenService.getWeb3j().ethChainId().send().getChainId().longValueExact();
        RawTransactionManager txManager = new RawTransactionManager(fewTokenService.getWeb3j(), credentials, chainId);

        // 3. DEPLOY
        log.info("🚀 Deploying FEW Token Contract...");
        FewToken contract = FewToken.deploy(
                fewTokenService.getWeb3j(),
                txManager,
                new DefaultGasProvider()
        ).send();
        
        String contractAddress = contract.getContractAddress();
        log.info("📄 Contract Address: {}", contractAddress);

        // 4. DB SETUP
        String bobAddress = "0x1234500000000000000000000000000000000002";
        userRepository.save(new User("Bob_Test", "bob@few.com", bobAddress));

        // 5. ACTION: TRANSFER
        BigDecimal amount = new BigDecimal("10.00");
        String txHash = fewTokenService.transfer(contractAddress, bobAddress, amount, masterKey);
        log.info("✅ Transfer Success! Hash: {}", txHash);

        // 6. PERSISTENCE
        Transaction dbTx = new Transaction(
            UUID.randomUUID(), 
            txHash, 
            credentials.getAddress(), 
            bobAddress, 
            amount, 
            "SUCCESS", 
            UUID.randomUUID(),
            LocalDateTime.now()
        );
        transactionRepository.save(dbTx);

        // 7. VERIFICATION
        BigDecimal balance = fewTokenService.getBalance(contractAddress, bobAddress, masterKey);
        log.info("💰 Bob Balance: {} FEW", balance);
        assertEquals(0, amount.compareTo(balance));
    }
}
