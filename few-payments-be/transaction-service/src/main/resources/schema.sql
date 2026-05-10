/**
 * schema.sql
 * Author: Edoardo Sabatini
 * Date: 2026-05-09
 * Latest Edit: 2026-05-10
 * Company: few-payments
 * Description: $FEW Payment System - Database Schema
 * Focus: High-precision transactions and SAGA pattern orchestration.
 */

-- Create users table to map identities to blockchain addresses
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    wallet_address VARCHAR(42) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create explicit table for transactions if not exists
CREATE TABLE IF NOT EXISTS transactions (
    -- Unique identifier for the database record
    id UUID PRIMARY KEY,
    
    -- Blockchain transaction reference (Geth integration)
    transaction_hash VARCHAR(255) UNIQUE NOT NULL,
    
    -- Wallet addresses for the $FEW token transfer
    sender_wallet VARCHAR(255) NOT NULL,
    receiver_wallet VARCHAR(255) NOT NULL,
    
    -- High precision numeric for crypto-assets (18 decimal places)
    amount NUMERIC(38, 18) NOT NULL,
    
    -- Current lifecycle state of the payment
    status VARCHAR(50) NOT NULL,
    
    -- Correlation ID for distributed transaction management (SAGA)
    saga_id UUID,
    
    -- Audit timestamp for record creation
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_wallet ON users(wallet_address);

-- Index to optimize lookups during SAGA compensation flows
CREATE INDEX IF NOT EXISTS idx_transactions_saga_id ON transactions(saga_id);

-- Index to optimize blockchain hash verification
CREATE INDEX IF NOT EXISTS idx_transactions_hash ON transactions(transaction_hash);
