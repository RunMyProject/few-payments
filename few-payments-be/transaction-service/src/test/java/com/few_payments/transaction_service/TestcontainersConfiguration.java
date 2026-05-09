package com.few_payments.transaction_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * TestcontainersConfiguration
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Defines a shared PostgreSQL container for all integration tests.
 *              @ServiceConnection automatically wires the container's URL, username,
 *              and password into Spring's datasource — no @DynamicPropertySource needed.
 *              The container starts once and is reused across the entire test suite.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    
    @Bean
    @ServiceConnection
    // Rimosso il <?> qui
    PostgreSQLContainer postgresContainer() {
        // Rimosso il <> qui
        return new PostgreSQLContainer("postgres:16-alpine");
    }
}
