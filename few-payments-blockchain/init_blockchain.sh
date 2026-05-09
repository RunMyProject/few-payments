#!/bin/bash
# init_blockchain.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Description: Initializes the Geth database with the Genesis Block.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BLOCKCHAIN_DIR="."
GENESIS_FILE="$BLOCKCHAIN_DIR/genesis.json"
DATA_DIR="$BLOCKCHAIN_DIR"

echo "--- BLOCKCHAIN: Initializing Genesis Block ---"

if [ ! -f "$GENESIS_FILE" ]; then
    echo "ERROR: genesis.json not found in $BLOCKCHAIN_DIR"
    exit 1
fi

# Inizializzazione
geth init --datadir "$DATA_DIR" "$GENESIS_FILE"

echo "--- BLOCKCHAIN INITIALIZED ---"
