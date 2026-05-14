package com.few_payments.transaction_service.transaction.service;

import org.springframework.stereotype.Service;
import com.few_payments.transaction_service.contracts.FewToken;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.tx.RawTransactionManager;

/*
 * FewTokenService.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-14
 * Company: few-payments
 * Description: Service for interacting with the FewToken smart contract, allowing us to check balances
 * and transfer tokens between users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FewTokenService {

    private final Web3j web3j;

    public String transfer(String contractAddress, String to, BigDecimal amount, String privateKey) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        
        // Retrieve Chain ID for EIP-155 protection
        // An EIP-155 is a mechanism to prevent replay attacks across different blockchains
        // by including the chain ID in the transaction signature.
        long chainId = web3j.ethChainId().send().getChainId().longValueExact();
        
        // USA RawTransactionManager con ChainID
        RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

        FewToken contract = FewToken.load(
                contractAddress,
                web3j,
                txManager,
                new DefaultGasProvider()
        );

        // Convert BigDecimal to BigInteger (18 decimals)
        BigInteger rawAmount = amount.multiply(new BigDecimal("1000000000000000000")).toBigInteger();

        log.info("💸 Trasferimento di {} FEW a {} (ChainID: {})", amount, to, chainId);
        return contract.transfer(to, rawAmount).send().getTransactionHash();
    }

    public BigDecimal getBalance(String contractAddress, String account, String privateKey) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        long chainId = web3j.ethChainId().send().getChainId().longValueExact();
        RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

        FewToken contract = FewToken.load(contractAddress, web3j, txManager, new DefaultGasProvider());
        BigInteger balance = contract.balanceOf(account).send();
        return new BigDecimal(balance).divide(new BigDecimal("1000000000000000000"));
    }

    public Web3j getWeb3j() {
        return web3j;
    }
}
