package com.few_payments.transaction_service.transaction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

// Notes on the balance precision:
// The precision of "Wei" is 18 decimal places,
// which can lead to very long decimal numbers when converted to "Ether"
// (or "FEW" in our case).
// If the balance in the Genesis file or mined is set with a power of 2
// (e.g., 2^20), the conversion to Ether can produce a long decimal
// with many digits.
// To make it more readable, we can round it to 2 decimal places when
// displaying the balance. This is purely for display purposes and does
// not affect the actual balance in the blockchain, which is still stored
// with full precision in Wei.

import java.math.RoundingMode;

/*
 * BlockchainService.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-10
 * Company: few-payments
 * Description: Service for interacting with the blockchain.
 */
@Service
public class BlockchainService {

    private final Web3j web3j;
    private final String masterWallet;

    // Spring injects the Web3j bean and the master wallet address from the configuration
    public BlockchainService(Web3j web3j, @Value("${blockchain.master-wallet}") String masterWallet) {
        this.web3j = web3j;
        this.masterWallet = masterWallet;
    }

    public BigDecimal getBalance() throws Exception {
        // Call to Geth to get the balance of the master wallet address
        EthGetBalance balanceResponse = web3j.ethGetBalance(masterWallet, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        BigDecimal etherBalance = Convert.fromWei(balanceResponse.getBalance().toString(), Convert.Unit.ETHER);
        
        // Convert from Wei (the atomic unit) to Ether (FEW in our case)
        // We round it to 2 decimal places for better readability
        return etherBalance.setScale(2, RoundingMode.HALF_UP);
    }
}