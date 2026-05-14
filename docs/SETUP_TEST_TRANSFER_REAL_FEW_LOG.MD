# `SETUP_TEST_TRANSFER_REAL_FEW_LOG.MD`

## 📅 Session 10: Thursday, May 14, 2026

**Objective:** ERC20 Smart Contract Deployment, EIP-155 Replay Protection, Automated Java Wrapper Generation, and Dual-State Persistence (Postgres + Geth).

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **16:45** | **Setup** | **Web3j Maven Plugin** | Configured `web3j-maven-plugin` in `pom.xml`. Automated the compilation of `FewToken.sol` into Java classes to eliminate manual ABI/Binary mapping errors. |
| **17:15** | **Development** | **Smart Contract Logic** | Refined `FewToken.sol` (ERC20). Defined the "FEW" token logic. Reset `data_dev` folder and Geth node to ensure a clean genesis state for contract deployment. |
| **17:45** | **Bug Fixing** | **EIP-155 Resolution** | Encountered `JsonRpcError -32000` (replay-protected transactions). Resolved by refactoring `FewTokenService` to dynamically fetch `chainId` (1337) and use `RawTransactionManager`. |
| **18:20** | **Tooling** | **Wallet Verification** | Implemented `KeyCheck.java` to validate that the private key in `.env` (ending in `...99a4`) correctly matches the Master Wallet address `0x71562b...`. |
| **18:50** | **Debugging** | **IDE & Lombok Sync** | Fixed "red code" in VS Code (e.g., `setIdD` typo). Executed `mvn clean compile` and reset Java Language Server to fix annotation processing for `Transaction` entity. |
| **19:15** | **Testing** | **ERC20 Flow Execution** | Programmed `AliceToBobFewTokenTest.java`. Successfully simulated: Deploy -> DB User Creation -> ERC20 Transfer -> Postgres Transaction Logging. |
| **19:45** | **Finalization** | **Build Success** | Achieved `BUILD SUCCESS`. Verified that Bob's balance on Geth matches the record saved in the PostgreSQL `transactions` table. |
| **20:10** | **Docs** | **Documentation Updated** | Updated README and internal logs with the current file structure and testing protocols. |

---

## 🛠️ Essential Setup: Smart Contract & Web3j Automation

To interact with ERC20 tokens, we moved beyond simple Layer 1 transfers. We now use a generated Java Wrapper that acts as a type-safe proxy for the Smart Contract.

1. **Solidity Source:** Place `FewToken.sol` in `src/main/resources/solidity`.
2. **Maven Generation:** Running `mvn generate-sources` triggers the Web3j plugin, creating `FewToken.java` in the generated-sources directory.
3. **Chain ID Protection:** In a private Geth network, the node strictly requires EIP-155 signatures.
* **Fix:** Use `RawTransactionManager(web3j, credentials, chainId)`.



---

## 💻 Technical Implementation & Method Documentation

### 1. `FewTokenService.java` (ERC20 Wrapper Service)

This service was re-engineered to handle Smart Contract interactions instead of simple ETH transfers.

* **`transfer(String contractAddress, String to, BigDecimal amount, ...)`**:
* **Dynamic Protection:** It executes `web3j.ethChainId().send()` to ensure the signature is valid for the current network.
* **Precision Conversion:** Converts `BigDecimal` (FEW) to `BigInteger` (Wei) using $10^{18}$ as the multiplier, matching the ERC20 standard.
* **Contract Loading:** Uses `FewToken.load()` to attach to the deployed address with the correctly signed `txManager`.



### 2. `AliceToBobFewTokenTest.java` (The Full Integration)

This test represents the complete "Double Truth" orchestration for the FEW ecosystem.

* **Phase 1: Contract Deployment:** The Master Wallet deploys the `FewToken` contract. This provides the `contractAddress` used for all subsequent steps.
* **Phase 2: Relational State:** Saves "Bob" into Postgres. This ensures we have a valid `User` entity to link with the upcoming blockchain transaction.
* **Phase 3: Cryptographic Execution:** Calls `fewTokenService.transfer()`. This is the point where the actual "money" moves on the Geth ledger.
* **Phase 4: State Reconciliation:** A `Transaction` object is instantiated and saved to Postgres, capturing the `txHash` as the definitive link between the SQL and Blockchain worlds.

---

## ✅ Core Concepts (Don't Forget!)

> [!IMPORTANT]
> **EIP-155 & Replay Protection**
> Geth nodes (version 1.10+) often default to rejecting transactions that don't include a `chainId`. This prevents a transaction from one network being "replayed" on another. Every `TransactionManager` in Java must be explicitly initialized with the `chainId` fetched from the node.

> [!NOTE]
> **ERC20 vs. Native Transfer**
> Unlike Session 9, where we moved "Native FEW" (Layer 1), we are now moving "Contract FEW" (Layer 2). This requires a deployed Smart Contract and uses `balanceOf()` calls instead of `eth_getBalance`.

> [!CAUTION]
> **IDE Persistence & Lombok**
> When modifying Entity classes (like `Transaction`), VS Code might not immediately see the new `@Data` methods. Always run `mvn clean compile` and use the "Clean Java Language Server" command if the code is correct but appears red.

---

### Useful Commands for the Terminal

**Check ERC20 Balance via Geth Console:**

```javascript
var abi = [...]; // FewToken ABI
var contract = eth.contract(abi).at("CONTRACT_ADDRESS");
contract.balanceOf("0x71562b...");

```

**Maven Test Filter:**

```bash
mvn test -Dtest=AliceToBobFewTokenTest

```

**Clean Reset (If Blockchain/DB Crash):**

1. Stop Docker Compose.
2. Delete `data_dev` folder.
3. Run `mvn clean compile`.
4. Restart Docker.

---

### the pom part to generate java files from solidity:

```xml
<plugin>
    <groupId>org.web3j</groupId>
    <artifactId>web3j-maven-plugin</artifactId>
    <version>4.12.1</version>
    <executions>
        <execution>
            <id>generate-sources-from-solidity</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>generate-sources</goal>
            </goals>
            <configuration>
                <soliditySourceFiles>
                    <directory>${project.basedir}/src/main/resources/solidity</directory>
                    <includes>
                        <include>**/*.sol</include>
                    </includes>
                </soliditySourceFiles>
                <outputDirectory>
                    <java>${project.build.directory}/generated-sources/web3j</java>
                </outputDirectory>
                <packageName>com.few_payments.transaction_service.blockchain.contracts</packageName>
            </configuration>
        </execution>
    </executions>
</plugin>		
```

---

*Note: This session (16:45 - 19:45) was a critical deep dive into EIP-155 security and automated contract generation. We successfully bridged Java's strict typing with Solidity's dynamic nature, ensuring that every FEW transfer is cryptographically signed, replay-protected, and mirrored in our PostgreSQL audit trail.*
