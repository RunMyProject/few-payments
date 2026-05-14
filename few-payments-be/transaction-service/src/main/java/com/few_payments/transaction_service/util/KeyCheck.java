package com.few_payments.transaction_service.util;

import org.web3j.crypto.Credentials;

/*
 * KeyCheck.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-14
 * Company: few-payments
 * Description: Utility class for checking Ethereum private keys and generating addresses.
 */
public class KeyCheck {
    public static void main(String[] args) {
        String pk = "b8c1b5c9331f005922a90231C39906665175a61d592202dfD00b5336d39999a4";
        Credentials credentials = Credentials.create(pk);
        System.out.println("Generated Address: " + credentials.getAddress());        
    }
}
