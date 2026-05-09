# `SETUP_DB_LOG.md`

## 📅 Session 4: Saturday, May 9, 2026

**Objective:** JPA Layer, REST Endpoint, DB Integration, and First Verified Read from PostgreSQL.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **13:00** | **Architecture** | **Package Structure** | Defined package root `com.few_payments.transaction_service`. Sub-packages: `controller/`, `transaction/entity/`, `transaction/repository/`, `transaction/service/`. |
| **13:05** | **Development** | **Transaction Entity** | Created `transaction/entity/Transaction.java`. Maps `transactions` table: `UUID id`, `String transactionHash`, `String senderWallet`, `String receiverWallet`, `BigDecimal amount` (`NUMERIC 38,18`), `String status`, `UUID sagaId`, `LocalDateTime createdAt`. |
| **13:10** | **Development** | **TransactionRepository** | Created `transaction/repository/TransactionRepository.java`. Extends `JpaRepository<Transaction, UUID>`. `findAll()` provided by Spring Data — zero boilerplate. |
| **13:15** | **Development** | **TransactionService** | Created `transaction/service/TransactionService.java`. `@Transactional(readOnly = true)` on `findAll()`. Logs count via SLF4J. |
| **13:20** | **Development** | **TransactionController** | Created `controller/TransactionController.java`. `GET /api/v1/transactions` → `SELECT * FROM transactions` via service layer. Returns `ResponseEntity<List<Transaction>>`. |
| **13:25** | **Config** | **application.properties** | Set `spring.sql.init.mode=always` (schema.sql runs on every startup), `spring.jpa.hibernate.ddl-auto=validate` (Hibernate checks schema, does not own DDL), datasource wired to `${POSTGRES_DB}`, `${POSTGRES_USER}`, `${POSTGRES_PASSWORD}`. |
| **13:30** | **Runtime** | **First Startup Attempt** | `./mvnw spring-boot:run` → **FAIL**: `PSQLException: FATAL: password authentication failed for user "${POSTGRES_USER}"`. Env vars not exported to shell. |
| **13:35** | **DevOps** | **12-Factor Fix (attempt 1)** | `export $(grep -v '^#' .env \| grep -v '^UID=' \| xargs)` → **FAIL**: `../.env` not found. `.env` located two levels up at `../../.env`. |
| **13:40** | **DevOps** | **12-Factor Fix (attempt 2)** | `set -a && source ../../.env && set +a && ./mvnw spring-boot:run` → **SUCCESS**. Spring Boot resolves all `${}` placeholders from OS environment. |
| **13:45** | **Runtime** | **Service Startup** | Microservice active on port `8080`. Hibernate validated schema against `transactions` table. HikariPool-1 started. `Found 1 JPA repository interface`. |
| **13:50** | **Verification** | **Write Test (psql)** | `sudo docker exec few-payments-db psql -U $POSTGRES_USER -d $POSTGRES_DB -c "INSERT INTO transactions ..."` — `INSERT 0 1`. Row with `transaction_hash = '0xabc123'`, `status = 'PENDING'`, `amount = 1.5`. |
| **13:55** | **Verification** | **Read Test (API)** | `curl http://localhost:8080/api/v1/transactions` → JSON array with 1 row. Spring log: `Found 1 transaction(s)`. |
| **14:00** | **DevOps** | **Test Script** | Created `few-payments-ops/transaction_test_write_read.sh`. Auto-discovers `.env` walking up 4 directory levels. Runs INSERT → SELECT (psql) → GET (curl) in sequence. `ON CONFLICT DO NOTHING` ensures idempotence. |
| **14:05** | **Verification** | **Script End-to-End** | `./transaction_test_write_read.sh` — `INSERT 0 0` (idempotent), psql SELECT returns 1 row, API returns matching JSON. Stack fully verified. |
| **14:10** | **Startup** | **Script Maven Start** | `./start_transaction_service.sh` |
 **14:15** | **Docs** | **Update Setup Log** | Finalized Session 4 documentation and verified web visibility. |
| **14:22** | **End Session** | _ | _ |

---

## ✅ Progress Status

As of May 9, 2026, 2:20 PM, the environment status is:

* **Microservice:** `transaction-service` is UP on port `8080`.
* **Database:** PostgreSQL container `few-payments-db` active, `transactions` table populated.
* **REST API:** `GET /api/v1/transactions` returns correct JSON from DB.
* **Test Script:** `transaction_test_write_read.sh` verifies write + read end-to-end.
* **12-Factor:** Config injected via environment — `set -a && source ../../.env && set +a && ./mvnw spring-boot:run`.

---

## 📁 File Structure

```
few-payments/
├── .env                                          # POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB
├── docker-compose.yml                            # postgres:16-alpine on port 5432
├── few-payments-be/
│   └── transaction-service/
│       └── src/main/java/com/few_payments/transaction_service/
│           ├── TransactionServiceApplication.java
│           ├── controller/
│           │   ├── HelloController.java
│           │   └── TransactionController.java    # GET /api/v1/transactions
│           └── transaction/
│               ├── entity/
│               │   └── Transaction.java          # @Entity — mirrors schema.sql
│               ├── repository/
│               │   └── TransactionRepository.java # JpaRepository<Transaction, UUID>
│               └── service/
│                   └── TransactionService.java   # @Transactional(readOnly = true)
└── few-payments-ops/
    └── postgres_test_connection.sh               # INSERT → SELECT → GET /api/v1/transactions + SELECT * FROM transactions via psql
```

---
