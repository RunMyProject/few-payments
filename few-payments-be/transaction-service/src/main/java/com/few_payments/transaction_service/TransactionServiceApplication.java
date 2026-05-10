package com.few_payments.transaction_service;

import com.few_payments.transaction_service.transaction.service.BlockchainService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

/*
 * TransactionServiceApplication.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Latest Edit: 2026-05-10
 * Company: few-payments
 * Description: Main class for the transaction service application.
 */
@SpringBootApplication
public class TransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner checkConfig(
        @Value("${blockchain.rpc-url}") String rpc, 
        @Value("${blockchain.master-wallet}") String wallet,
        BlockchainService blockchainService) { 
			// We inject the blockchain service to test the connection at startup
        
        return args -> {
            System.out.println("\n🌐 --- [BLOCKCHAIN STATUS] --- 🌐");
            System.out.println("RPC URL: " + rpc);
            System.out.println("MASTER WALLET: " + wallet);
            
            try {
                // Chiamata reale al nodo Geth
                BigDecimal balance = blockchainService.getBalance();
                System.out.println("CURRENT BALANCE: " + balance + " FEW");
            } catch (Exception e) {
                System.err.println("❌ ERROR CONNECTING TO GETH: " + e.getMessage());
                System.err.println("Make sure your Geth node is running at: " + rpc);
            }
            
            System.out.println("------------------------------\n");
            
            if (wallet == null || wallet.isEmpty()) {
                System.err.println("⚠️ PAY ATTENTION: Master wallet address is not set!");
            }
        };
    }
}
