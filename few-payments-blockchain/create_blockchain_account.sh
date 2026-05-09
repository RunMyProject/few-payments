#!/bin/bash
# create_blockchain_account.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Company: few-payments
# Description: Generates a new Ethereum account for the FewCoin network.
#              The keystore will be saved in the dedicated blockchain data directory.

# 12-Factor: Load environment variables from .env file
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE=""
SEARCH_DIR="$SCRIPT_DIR"
for i in 1 2 3 4; do
    if [ -f "$SEARCH_DIR/.env" ]; then
        ENV_FILE="$SEARCH_DIR/.env"
        break
    fi
    SEARCH_DIR="$(dirname "$SEARCH_DIR")"
done

if [ -z "$ENV_FILE" ]; then
    echo "ERROR: .env file not found."
    exit 1
fi

echo "Loading environment from: $ENV_FILE"

# Note: We exclude UID and GID to avoid issues with permissions when running geth as a different user.

set -a
grep -v '^#' "$ENV_FILE" | grep -v '^UID=' | grep -v '^GID=' > /tmp/few_env && source /tmp/few_env && rm /tmp/few_env
set +a

# Path to the blockchain data directory where the keystore will be saved
BLOCKCHAIN_DATA_DIR="."

echo "--- BLOCKCHAIN: Creating New Authority Account ---"
echo "Directory: $BLOCKCHAIN_DATA_DIR"

# Execute the command to create a new Ethereum account. 
# The user will be prompted to enter a password for the account. 
# The keystore file will be saved in the specified data directory.
# Note: The geth command must be available in the system's PATH.
# Ensure that geth is installed and properly configured before running this script.
geth account new --datadir "$BLOCKCHAIN_DATA_DIR"

echo "--- ACCOUNT CREATED ---"
echo "IMPORTANT: Save the Address and the Password you just entered!"
echo "The keystore file is located in $BLOCKCHAIN_DATA_DIR/keystore"
