#!/bin/bash
# clean_db.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Company: few-payments
# Description: Cleans the PostgreSQL database by dropping and recreating the public schema.
#     
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

grep -v '^#' "$ENV_FILE" | grep -v '^UID=' | grep -v '^GID=' > /tmp/few_env && set -a && source /tmp/few_env && set +a && rm /tmp/few_env

set +a

echo "--- CLEANING DB: dropping and recreating public schema ---"
sudo docker exec few-payments-db psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "TRUNCATE TABLE transactions RESTART IDENTITY;"
echo "--- DB CLEANED ---"
