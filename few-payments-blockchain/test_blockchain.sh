#!/bin/bash
# test_blockchain.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Description: Sends a JSON-RPC request to the Geth node to verify connectivity.

echo "--- BLOCKCHAIN: Testing Connection to Geth Node ---"

# This curl command asks Geth: "Which accounts do you have?"
# -X POST: Send a POST request
# --data: The JSON command for the blockchain
# -H: Set the header to JSON
curl -X POST \
     --data '{"jsonrpc":"2.0","method":"eth_accounts","params":[],"id":1}' \
     -H "Content-Type: application/json" \
     http://localhost:8545

echo -e "\n--- TEST COMPLETED ---"
