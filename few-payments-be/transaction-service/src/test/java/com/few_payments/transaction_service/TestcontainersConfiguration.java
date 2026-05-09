package com.few_payments.transaction_service;

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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine");
    }
}
