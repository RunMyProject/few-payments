#!/bin/bash
# start_transaction_service.sh
# Author: Edoardo Sabatini
# Date: 2026-05-09
# Company: few-payments
# Description: Loads environment variables from the root .env file and starts
#              the transaction-service Spring Boot application via Maven.
#              12-Factor III: config comes from the environment, never hardcoded.

# ── Locate .env walking up the directory tree ───────────────────────────────────
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
    echo "ERROR: .env file not found"
    exit 1
fi

echo "Load environment variables from: $ENV_FILE"
set -a
source "$ENV_FILE"
set +a

# ── Locate transaction-service using project root (where .env lives) ────────────
PROJECT_ROOT="$(dirname "$ENV_FILE")"
SERVICE_DIR="$PROJECT_ROOT/few-payments-be/transaction-service"

if [ ! -f "$SERVICE_DIR/mvnw" ]; then
    echo "ERROR: mvnw not found in $SERVICE_DIR"
    exit 1
fi

# ── Launch Spring Boot ───────────────────────────────────────────────────────────
echo "Starting transaction-service from: $SERVICE_DIR"
echo "Connecting to: jdbc:postgresql://localhost:5432/$POSTGRES_DB as $POSTGRES_USER"
echo ""

cd "$SERVICE_DIR" && ./mvnw spring-boot:run