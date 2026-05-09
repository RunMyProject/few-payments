#!/bin/bash
# start_fewcoin_node.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Description: Starts Geth in Developer Mode. 
#              Automates block production and account unlocking.

BLOCKCHAIN_DIR="."
DATA_DIR="$BLOCKCHAIN_DIR/data_dev"

echo "--- BLOCKCHAIN: Starting FewCoin Node (DEV MODE) ---"

# We use --dev to avoid PoS/Merge complexity during local development
geth --datadir "$DATA_DIR" \
     --dev \
     --dev.period 0 \
     --http \
     --http.addr "0.0.0.0" \
     --http.port 8545 \
     --http.api "eth,net,web3,debug,miner" \
     --http.corsdomain "*"
