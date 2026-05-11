# `SETUP_TEST_TRANSFER_WALLET_LOG.MD`

## 📅 Session 9: Monday, May 11, 2026

**Objective:** Real Blockchain Transaction Integration, Key Extraction Tooling, Web3j Method Engineering, and Logic Persistence.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **09:00** | **Research** | **Web3j & EIP-155 Study** | Deep dive into Ethereum transaction signing mechanisms. Discovered that hardcoding a standard `chainId` can cause transaction rejections or open the network to replay vulnerabilities. Engineered a dynamic chainId extraction solution. |
| **10:15** | **Brainstorming** | **Architectural Logic** | Defined the "Double Truth": Funds live on Geth (Immutable Blockchain State), Identity lives on Postgres (RDBMS State). Resolved why Java strictly requires the Raw Private Key for EIP-155 compliant signing instead of relying on an unlocked node configuration. |
| **11:30** | **Analysis** | **Wei to Decimal Precision** | Studied `BigDecimal` precision mechanics and Ethereum atomic units. Wei has 18 decimals; converting balances structured around powers of 2 (e.g., $2^{20}$ Wei) to Ether results in infinite fractions. Enforced strict `RoundingMode.HALF_UP` to prevent display overflow and precision crashes. |
| **14:15** | **Bug Fixing** | **Refactoring DI** | Cleaned `TransactionControllerTest.java` and `FewPaymentIntegrationTest.java`. Migrated from Field Injection to Constructor Injection utilizing `@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)` to guarantee immutable dependencies (`final` fields) and bulletproof Spring test boot lifecycles. |
| **14:45** | **Setup** | **Node.js Environment** | `npm install keythereum`. This was strictly required to unlock the Geth keystore, as Web3j's native wallet JSON parsing was throwing encryption algorithm version mismatches with the latest Geth block format. |
| **15:30** | **Tooling** | **Key Extraction** | Created `extractor.js` using the keythereum library. Securely recovered the hex Private Key for the Master Wallet, bypassing local Geth RPC unlocking restrictions to allow Java to sign transactions entirely offline. |
| **16:00** | **Development** | **Service Implementation** | Engineered `BlockchainService.java`. Designed the `transferFew` and `getBalanceOf` methods, mapping complex Web3j payload logic to a clean internal Domain logic. |
| **16:15** | **Manual Seed** | **Initial Funding** | Seeded Alice with **10,000 FEW** via Geth IPC console to verify the "Master-to-User" bridge and extensively test the new precision calculations. |
| **17:00** | **Testing** | **Integration Test Logic** | Programmed `AliceToBobBlockchainTest.java` and `BlockchainIntegrationTest.java`. Formalized terminal outputs with `💰` and `🏦` symbols for instant readability inside CI/CD logging environments. |
| **18:10** | **Docs** | **Documentation Updated** | Updated README and internal logs with the current file structure and testing protocols. |

---

## 🛠️ Essential Setup: Key Extraction & Node.js

To independently sign offline transactions via Web3j (which is mandatory for production-grade security and EIP-155 compliance), the Java backend explicitly needs the **Raw Private Key**. Relying on `geth --unlock` exposed over RPC is an extreme anti-pattern for a microservices architecture.

1. `cd few-payments-blockchain`
2. `npm install keythereum`
3. Run `node extractor.js` to derive and output the raw hex string from the encrypted keystore JSON.
4. Copy and paste it into `application.properties`: `blockchain.master-private-key=...` *(Note: In a live production environment, this must be securely injected via Vault or AWS KMS)*.

---

## 💻 Technical Implementation & Method Documentation

The implementation phase required hours of profound architectural study on how to safely bridge a deterministic cryptographic ledger (Geth) with a traditional CRUD system (PostgreSQL), ensuring absolute data consistency against race conditions and precision errors.

### 1. `BlockchainService.java`
This service operates as the core gateway bridging the Java backend to the Web3j node interface. Every method has been engineered after careful review of Web3j constraints to handle edge cases associated with blockchain interactions.

* **`getBalance()` & `getBalanceOf(String address)`**:
  * **The Problem:** The EVM (Ethereum Virtual Machine) natively calculates balances in *Wei* (18 decimal places of scope). Standard Java arithmetic operations inherently trigger precision loss or `ArithmeticException` (non-terminating decimal expansions) when manipulating balances minted during Genesis (like $2^{20}$).
  * **The Solution:** The method fetches the raw atomic Wei value via `web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)`. The logic relies safely on `Convert.fromWei(..., Convert.Unit.ETHER)` to scale the value up to our internal `FEW` token representation. 
  * **The Failsafe Formatting:** We enforced a strict `.setScale(2, RoundingMode.HALF_UP)` pipeline return. This explicitly guarantees that UI endpoints and application logs stay readable while the blockchain mathematically locks down the full 18-decimal precision under the hood.

* **`transferFew(String toAddress, BigDecimal amountInFew, String privateKey)`**:
  * **The Problem:** Earlier iterations and legacy Web3j documentation relied on unlocked nodes or basic `TransactionManager` classes without enforcing chain ID signatures. This creates a lethal security vulnerability known as a **Replay Attack** (EIP-155). Furthermore, hardcoding the `chainId` into the service variables makes deployments across different environments incredibly brittle.
  * **The Solution:** 
    1. **Dynamic Chain Resolution:** The code dynamically executes `web3j.ethChainId().send().getChainId().longValueExact()` on the fly right before signing. This guarantees every transaction is protected by the exact ID of the *currently attached* node in the Docker network.
    2. **Offline Credentials Validation:** By loading `Credentials.create(privateKey)`, we build the signature envelope locally. The private key is never transported across the network.
    3. **FastRawTransactionManager:** This manager bypasses slow local node nonce-queues, validating and assembling the raw transaction entirely on the backend server before broadcasting it into Geth's memory pool.
    4. **Execution Abstraction:** The transfer relies on `Transfer.sendFunds(..., Convert.Unit.ETHER)`, safely abstracting nonce management and gas pricing estimates.
  * **Return Guarantee:** The method responds with the absolute source of truth: the resulting `TransactionHash`. This string is the sole cryptographic proof that the network protocol successfully recognized and processed the intent.

### 2. Integration Testing Suite
Testing blockchain states requires a fundamental paradigm shift compared to RDBMS. The local Geth Docker instance inherently maintains long-term persistence across JUnit runs.

* **`BlockchainIntegrationTest.java`**:
  * A foundational check ensuring the Web3j transport layer resolves against the Docker node endpoints. It verifies that the `Master Wallet` exists, is active, and possesses initial funds via the custom genesis block. It tests our conversion logic by rigorously asserting that the resultant `BigDecimal` prints seamlessly using `@Slf4j` logs.

* **`AliceToBobBlockchainTest.java` (The "Double Truth" Flow)**:
  * A full-cycle End-to-End integration simulating the live infrastructure.
  * **Phase 1 (Postgres Assertion):** Seeds relational constraint validation by instantiating a local DB `User` (Bob) linked strictly to an accurate `0x...` hex destination.
  * **Phase 2 (Geth Execution):** Emits the signed cryptographic transfer payload via `transferFew()`. The process validates Web3j responses by tracking the dynamic chain check `🔗 Actual Geth Chain ID: ...` and confirming block acceptance.
  * **Phase 3 (State Reconciliation Validation):** Validates the "Double Truth". Aggregates resulting transaction balances dynamically from Alice (Master), Bob (Receiver target), and ensures PostgreSQL effectively tracks the mirrored representation of reality.
  * **Formatted Metric Output:** Purpose-built UI logs injected for high-visibility during CI/CD monitoring sweeps:
    ```text
    ------------------------------------------
    💰 ALICE BALANCE: {} FEW
    💰 BOB BALANCE:   {} FEW
    🏦 MASTER BALANCE: {} FEW
    ------------------------------------------
    ```

---

## ✅ Core Concepts (Don't Forget!)

> [!IMPORTANT]
> **State Accumulation vs. DB Rollback**
> Unlike standard `@Transactional` SQL tests arrays, **Geth's state is aggressively persistent across compilations**. If `AliceToBobBlockchainTest` is triggered twice organically, Bob receives 5 + 5 = **10 FEW**. The blockchain never "rolls back" its memory to zero. Every execution interacts with the live cryptographic ledger on disk. This is a critical factor when managing logic inside continuous integration pipelines.

> [!NOTE]
> **Layer 1 Mechanics - No Smart Contract Engine Used**
> Current implementations operate purely on native gas variables—there is **no automated conversion via ERC20 contracts**. `1 FEW` operates functionally identical to `1 ETH` compiled natively into the `genesis.json`. We are transferring native base-layer tokens directly. The "Master Wallet" functions autonomously as an all-powerful Central Bank identity that successfully survives network reboots, provided the Docker `data_dev` volume persists.

> [!CAUTION]
> **Manual Geth Protocol & Asynchrony Rules**
> The internal Java Web3j wrappers simulate the terminal flow of `geth attach` -> `eth.sendTransaction(...)`.
> **Crucially:** The connection remains profoundly asynchronous. The internal creation of the `TransactionReceipt` implicitly stops thread execution to wait entirely for the target block configuration to be mined onto the latest header. **If you don't receive the resulting Transaction Hash text, the money has *not* moved.** The hash acts as our definitive execution lock. 

---

'''
geth attach http://localhost:8545
'''

'''
eth.sendTransaction({
  from: "0x71562b71999873db5b286df957af199ec94617f7",
  to: "0x7677afeb70f24580f72b2a259b0e956c3cbe4374",
  value: web3.toWei(10000, "ether")
})
'''

'''
# Master private key for signing transactions. Must be kept secret in production!
blockchain.master-private-key=0be09cc1ced9001882414f166a036f48f1304366133d1fdb63e2300dfb93e2c5
'''

'''
# Retrieved from genesis.json of the private blockchain, used to prevent replay attacks across chains.
blockchain.chain-id=12345
'''

'''java
    // This method executes a transfer of FEW from the master wallet to a specified address.
    public String transferFew(String toAddress, BigDecimal amountInFew, String privateKey) throws Exception {

        Credentials credentials = Credentials.create(privateKey);

        // It reads the chainId directly from the Geth node — no hardcoded value
        // 1. Load credentials from the private key
        long networkChainId = web3j.ethChainId().send().getChainId().longValueExact();

        // 2. We execute the actual transfer on Geth. The amount is converted from FEW to Wei (the atomic unit).
        // Convert.Unit.ETHER equivales to our "FEW" unit (18 decimals)
        Transfer transfer = new Transfer(
            web3j,
            new org.web3j.tx.FastRawTransactionManager(web3j, credentials, networkChainId)
        );

        // 3. I use the full version of sendFunds that accepts the chainId (EIP-155) to ensure 
        // compatibility with our private blockchain and prevent replay attacks across chains.
        TransactionReceipt receipt = transfer
            .sendFunds(toAddress, amountInFew, Convert.Unit.ETHER)
            .send();

        // 4. We return the transaction hash generated by the blockchain        
        return receipt.getTransactionHash();
    }
'''

'''java
    BigDecimal bobBalance = blockchainService.getBalanceOf(bobAddress);
    BigDecimal aliceBalance = blockchainService.getBalanceOf(aliceAddress);
    BigDecimal masterBalance = blockchainService.getBalance();
'''

'''java
    // This method retrieves the balance of any given address, not just the master wallet.
    public BigDecimal getBalanceOf(String address) throws Exception {
        EthGetBalance balanceResponse = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return Convert.fromWei(balanceResponse.getBalance().toString(), Convert.Unit.ETHER)
                .setScale(2, RoundingMode.HALF_UP);
    }
'''

---

*Note: Achieving this implementation required intense study. Reconciling dependency injection complexities, EIP-155 protection specs, Wei/Ether conversion math overflow anomalies, and designing an architecture that correctly partitions the state machine execution across PostgreSQL models versus raw Geth node endpoints required extensive effort. This double-layer mapping logic now seamlessly forms the secure backbone of the entire transaction orchestration engine.*
