package com.few_payments.transaction_service.controller;

import com.few_payments.transaction_service.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TransactionControllerIT
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Company: few-payments
 * Description: Integration test for GET /api/v1/transactions.
 *              Uses the shared PostgreSQL container defined in TestcontainersConfiguration.
 *              @Import replaces @Container + @DynamicPropertySource — the container is
 *              started once for the whole test suite and reused across all IT classes.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM transactions");
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactions() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnTransactionInsertedDirectlyInDb() throws Exception {
        jdbcTemplate.execute("""
                INSERT INTO transactions (id, transaction_hash, sender_wallet, receiver_wallet, amount, status)
                VALUES (gen_random_uuid(), '0xdeadbeef', '0xSender', '0xReceiver', 1.500000000000000000, 'PENDING')
                """);

        mockMvc.perform(get("/api/v1/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].transactionHash", is("0xdeadbeef")))
                .andExpect(jsonPath("$[0].senderWallet",    is("0xSender")))
                .andExpect(jsonPath("$[0].receiverWallet",  is("0xReceiver")))
                .andExpect(jsonPath("$[0].status",          is("PENDING")))
                .andExpect(jsonPath("$[0].sagaId",          nullValue()));
    }

    @Test
    void shouldReturnMultipleTransactions() throws Exception {
        jdbcTemplate.execute("""
                INSERT INTO transactions (id, transaction_hash, sender_wallet, receiver_wallet, amount, status)
                VALUES (gen_random_uuid(), '0xaaa111', '0xAlice', '0xBob',   10.0, 'CONFIRMED'),
                       (gen_random_uuid(), '0xbbb222', '0xBob',   '0xCarol', 20.0, 'PENDING')
                """);

        mockMvc.perform(get("/api/v1/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
