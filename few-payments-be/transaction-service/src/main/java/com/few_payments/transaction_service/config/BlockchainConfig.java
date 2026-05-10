package com.few_payments.transaction_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/*
 * BlockchainConfig.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-10
 * Company: few-payments
 * Description: Configuration class for the blockchain service.
 */
@Configuration
public class BlockchainConfig {

    @Bean
    public Web3j web3j(@Value("${blockchain.rpc-url}") String rpcUrl) {
        // We build the client that points to your local Geth node
        return Web3j.build(new HttpService(rpcUrl));
    }
}
