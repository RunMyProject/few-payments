package com.few_payments.transaction_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * TransactionServiceApplicationTests
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Latest Edit: 2026-05-10
 * Company: few-payments
 * Description: Basic context load test to verify that the Spring application starts correctly.
 * 			The @Import annotation loads the Testcontainers configuration, which starts
 * 		the PostgreSQL container before the context is initialized. If the container fails to start,
 * 		this test will fail, providing an early indication of issues with the test environment.
 */
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@SpringBootTest
class TransactionServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
