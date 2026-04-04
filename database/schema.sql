-- This file creates all tables needed for the FTMS system
-- Run this file in DBeaver on your local MySQL or on Aiven MySQL

CREATE DATABASE IF NOT EXISTS ftms_db;
USE ftms_db;

-- USERS TABLE: Stores all users including admin, central bank, commercial bank, importers, exporters
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,           -- stored as BCrypt hash, never plain text
    role ENUM('ADMIN', 'CENTRAL_BANK', 'COMMERCIAL_BANK', 'IMPORTER', 'EXPORTER', 'EXCHANGER') NOT NULL,
    kyc_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    role_selected BOOLEAN DEFAULT FALSE,      -- has user chosen their role after KYC approval?
    city VARCHAR(100),
    address TEXT,
    phone VARCHAR(20),
    bank_name VARCHAR(100),                   -- which commercial bank this user is linked to
    account_number VARCHAR(50),
    swift_code VARCHAR(20),                   -- for international transfers
    ifsc_code VARCHAR(20),                    -- for domestic transfers
    passport_data LONGTEXT,                   -- Base64 encoded passport image
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TRANSACTIONS TABLE: Every forex transaction is recorded here
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,                  -- who initiated this transaction
    transaction_type ENUM('IMPORT', 'EXPORT', 'EXCHANGE') NOT NULL,
    from_currency VARCHAR(10) NOT NULL,       -- e.g. INR
    to_currency VARCHAR(10) NOT NULL,         -- e.g. USD
    from_amount DECIMAL(15, 2) NOT NULL,      -- amount user is sending
    to_amount DECIMAL(15, 2),                 -- calculated amount user will receive
    exchange_rate DECIMAL(15, 6),             -- rate used at time of transaction
    purpose TEXT,                             -- reason: import payment to XYZ
    beneficiary_name VARCHAR(100),
    beneficiary_bank VARCHAR(100),
    beneficiary_swift VARCHAR(20),
    status ENUM('PENDING_CENTRAL_BANK', 'APPROVED_BY_CENTRAL_BANK', 'REJECTED_BY_CENTRAL_BANK', 'VERIFIED_BY_BANK', 'COMPLETED', 'FAILED') DEFAULT 'PENDING_CENTRAL_BANK',
    central_bank_approved_by BIGINT,          -- user id of central bank officer who approved
    bank_verified_by BIGINT,                  -- user id of bank officer who verified
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- EXCHANGE RATES TABLE: For storing fetched or manually set rates
CREATE TABLE exchange_rates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    base_currency VARCHAR(10) NOT NULL,       -- USD
    target_currency VARCHAR(10) NOT NULL,     -- INR
    rate DECIMAL(15, 6) NOT NULL,
    source VARCHAR(50) DEFAULT 'API',
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password is BCrypt of Admin@123)
INSERT INTO users (full_name, email, password, role, kyc_status, role_selected) VALUES 
('System Admin', 'admin@ftms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lmmG', 'ADMIN', 'APPROVED', TRUE),
('Central Bank Officer', 'centralbank@ftms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lmmG', 'CENTRAL_BANK', 'APPROVED', TRUE),
('Commercial Bank Officer', 'bank@ftms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lmmG', 'COMMERCIAL_BANK', 'APPROVED', TRUE);
-- All three default passwords are Admin@123
