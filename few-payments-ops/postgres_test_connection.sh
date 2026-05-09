#!/bin/bash
# postgres_test_connection.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Company: few-payments
# Description: Inserts a test transaction directly into PostgreSQL,
#              then reads it back via the Spring Boot REST API.

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
    echo "ERROR: .env file not found in script directory or parent directories."
    exit 1
fi

echo "Load environment variables from: $ENV_FILE"
set -a

# sostituisci la riga source con questa
grep -v '^#' "$ENV_FILE" | grep -v '^UID=' | grep -v '^GID=' > /tmp/few_env && set -a && source /tmp/few_env && set +a && rm /tmp/few_env

set +a

echo ""
echo "--- WRITE: inserting test transaction into $POSTGRES_DB ---"
sudo docker exec few-payments-db psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "
INSERT INTO transactions (id, transaction_hash, sender_wallet, receiver_wallet, amount, status)
VALUES (gen_random_uuid(), '0xabc123', '0xSenderWallet', '0xReceiverWallet', 1.500000000000000000, 'PENDING')
ON CONFLICT (transaction_hash) DO NOTHING;
"

echo ""
echo "--- READ (psql): direct DB query ---"
sudo docker exec few-payments-db psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "SELECT * FROM transactions;"

echo ""
echo "--- READ (API): via Spring Boot GET /api/v1/transactions ---"
curl -s http://localhost:8080/api/v1/transactions | python3 -m json.tool

echo ""
echo "--- TEST COMPLETE ---"
