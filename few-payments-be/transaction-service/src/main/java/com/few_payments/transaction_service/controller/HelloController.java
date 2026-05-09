package com.few_payments.transaction_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * HelloController.java
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Simple REST controller for testing the transaction service.
 * Provides a basic endpoint to verify that the service is running correctly.
*/
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "<h1>Hello few-payments!</h1>";
    }
}
