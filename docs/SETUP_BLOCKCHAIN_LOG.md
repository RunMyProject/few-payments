# `SETUP_BLOCKCHAIN_LOG.md`

## 📅 Session 6: Saturday, May 9, 2026

**Objective:** Blockchain Infrastructure Setup (Geth), Wallet Generation, and Node Initialization.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **18:10** | **Brainstorming** | **Blockchain Strategy** | Analyzed Geth v1.17.2 capabilities. Decided on a private network setup (ChainID 12345) to avoid public gas fees. |
| **18:25** | **Setup** | **Geth Installation** | Verified Geth installation on Linux Mint. Created dedicated directory structure: `few-payments-blockchain/`. |
| **18:35** | **Security** | **Account Creation** | Created `create_blockchain_account.sh`. Generated Master Wallet: **0x7677...**. Saved keystore and `password.txt`. |
| **18:45** | **Architecture** | **Genesis Config** | Created `genesis.json` with Clique PoA rules. Initialized blockchain state via `init_blockchain.sh`. |
| **18:50** | **Troubleshoot** | **Merge Resolution** | Encountered "PoS/Merge" errors in Geth 1.17. Identified that legacy mining is deprecated in newer versions. |
| **18:55** | **Pivot** | **Developer Mode** | Switched to `--dev` mode in `start_fewcoin_node.sh`. This provides instant block sealing and an auto-unlocked test account. |
| **19:00** | **Verification** | **RPC Connectivity** | Created `test_blockchain.sh`. Confirmed server is listening on port **8545** and responding to JSON-RPC calls. |
| **19:20** | **Docs** | **Finalizing Logs** | Updated Session 6 documentation. |

---

## ✅ Progress Status

As of May 9, 2026, 7:00 PM, the environment status is:

* **Blockchain Node:** **ONLINE** (Running in `--dev` mode).
* **RPC Endpoint:** `http://localhost:8545` active and responding.
* **Master Wallet:** Generated and secured in `/keystore`.
* **Connectivity:** Verified via `curl` (JSON-RPC 2.0).

---

## 📁 File Structure (Blockchain)

```text
few-payments-blockchain/
├── create_blockchain_account.sh  # Generates new wallets/keys
├── init_blockchain.sh            # Inits the DB with genesis rules
├── start_fewcoin_node.sh         # The "Engine": starts the server
├── test_blockchain.sh            # The "Tester": verifies with curl
├── genesis.json                  # The "Constitution" of your network
├── password.txt                  # Plaintext password for auto-unlock
├── keystore/                     # Encrypted private keys (DO NOT LOSE)
└── data_dev/                     # The actual blockchain database (blocks)

```

> [!IMPORTANT]
> **Getting started: Install Geth before launching scripts**
> To run the blockchain node on Linux Mint, you must first install the Ethereum suite via the official PPA:
> 1. **Add Repository:** `sudo add-apt-repository -y ppa:ethereum/ethereum`
> 2. **Update:** `sudo apt-get update`
> 3. **Install:** `sudo apt-get install ethereum`
> 4. **Verify:** Run `geth version` to confirm version **1.17.2-stable** or higher is active.
> 
> 

---

> [!IMPORTANT]
> **What we have built:**
> We haven't "named" the FewCoin yet. What you have now is a **Blockchain Node (Server)**. Think of it as a "private digital vault" that is currently running in "Developer Mode". It generates a temporary currency that we will **label** as **$FEW** inside our Java application. It's an engine waiting for orders.

---
