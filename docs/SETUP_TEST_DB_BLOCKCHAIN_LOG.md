# `SETUP_TEST_DB_BLOCKCHAIN_LOG.md`

## 📅 Session 8: Sunday, May 10, 2026

**Objective:** Consolidation of the Testing Suite and Environment Synchronization.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **14:15** | **Brainstorming** | **Testing Architecture** | Evaluated the decoupling of the Integration Test suite from the Geth node. Strategy: Use `@ActiveProfiles("test")` and Mock/Stub for Blockchain services. |
| **15:00** | **Bug Fixing** | **Context Alignment** | Fixed `BeanCreationException` by adding `@Profile("!test")` to `checkConfig`. Standardized test naming convention (added `Test` suffix) for IDE discovery. |
| **15:45** | **Brainstorming** | **Persistence Sync** | Refined the mapping between `schema.sql` and IT classes. Ensured `JdbcTemplate` cleanup logic is consistent across all test environments. |
| **16:30** | **Docs** | **Documentation Update** | Updated README and internal logs with the current file structure and testing protocols. |

---

## ✅ Progress Status

As of May 10, 2026, 4:00 PM, the environment status is:

* **Testing Suite:** **FULLY OPERATIONAL**. All Integration Tests (IT) and Unit Tests now pass both in Maven and VS Code.
* **Profile Management:** Correct separation between `default` (Geth active) and `test` (H2/Postgres-TC and mocked blockchain) environments.
* **Infrastructure:** Testcontainers configuration is stable and shared across the suite to optimize resource usage.

---

## 📁 Updated File Structure (Core & Testing)

```text
few-payments-be/transaction-service/
├── src/main/resources/
│   ├── application.properties      # Main configuration (RPC/Geth active)
│   └── schema.sql                  # Database DDL (PostgreSQL)
├── src/test/resources/
│   └── application-test.properties # Test-specific overrides
└── src/test/java/com/few_payments/transaction_service/
    ├── controller/
    │   └── TransactionControllerIT.java # Full API integration test
    ├── transaction/
    │   └── FewPaymentIntegrationTest.java # Business logic test
    └── TransactionServiceApplicationTests.java # Context loading test

```

---

> [!IMPORTANT]
> **The Story of the Testing Suite: Solving the "IDE vs Maven" Paradox**
> During this session, we resolved a critical misalignment between the command-line build and the IDE environment.
> 1. **Suffix Importance:** We discovered that classes without the `Test` or `IT` suffix were being ignored by the IDE's test runner, leading to false positives or missed execution.
> 2. **Context Guarding:** The `checkConfig` bean, while useful for startup diagnostics, was causing failures during tests by trying to reach the Geth node. Implementing `@Profile("!test")` ensures the application context remains "lightweight" during verification.
> 3. **Implicit Dependencies:** By explicitly defining `blockchain.master-wallet` and `rpc-url` in `application-test.properties`, we've made the test suite resilient and independent of the developer's local `.env` file.
> 4. **Persistence Integrity:** The entities `Transaction` and `User` (managed via `UserRepository`) are now verified against a real schema in `schema.sql`, preventing regressions in the data layer.
> 
> 

---
