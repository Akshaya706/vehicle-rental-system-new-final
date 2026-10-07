-- ========================================================
-- Vehicle Rental Management System - MySQL Database Schema
-- Based on IEEE 830 SRS Document Section 6 (Data Requirements)
-- Prepared by: AKSHAYA.R (B.Tech IT)
-- ========================================================

CREATE DATABASE IF NOT EXISTS vehicle_rental_db;
USE vehicle_rental_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- ADMIN, RENTAL_MANAGER, CUSTOMER
    full_name VARCHAR(150),
    email VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customer Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    driving_licence VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Vehicle Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type VARCHAR(50) NOT NULL, -- Car, Bike, SUV, Sedan, Luxury, Electric
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    registration_no VARCHAR(100) NOT NULL UNIQUE,
    daily_rate DOUBLE NOT NULL,
    availability VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, BOOKED, RENTED, UNDER_MAINTENANCE
    last_service_date DATE,
    insurance_expiry DATE,
    pollution_expiry DATE,
    seating_capacity INT DEFAULT 5,
    fuel_type VARCHAR(50) DEFAULT 'Petrol',
    transmission VARCHAR(50) DEFAULT 'Automatic',
    image_url VARCHAR(500)
);

-- Booking Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    booking_date DATE NOT NULL,
    rental_start_date DATE NOT NULL,
    rental_end_date DATE NOT NULL,
    booking_status VARCHAR(50) NOT NULL DEFAULT 'PENDING_APPROVAL', -- PENDING_APPROVAL, APPROVED, REJECTED, ACTIVE, COMPLETED, CANCELLED
    total_charge DOUBLE NOT NULL,
    base_daily_rate DOUBLE,
    rental_days INT,
    dynamic_multiplier DOUBLE DEFAULT 1.0,
    extra_charges DOUBLE DEFAULT 0.0,
    actual_return_date DATE,
    return_odometer INT,
    return_fuel_level VARCHAR(50),
    late_fee DOUBLE DEFAULT 0.0,
    manager_notes VARCHAR(500),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
);

-- Payment Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- Credit Card, Debit Card, UPI, Net Banking
    amount DOUBLE NOT NULL,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PAID', -- PAID, PENDING, REFUNDED
    payment_date DATE NOT NULL,
    transaction_ref VARCHAR(100),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
);

-- Damage Report Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS damage_report (
    damage_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    damage_description VARCHAR(500) NOT NULL,
    repair_cost DOUBLE NOT NULL,
    inspection_date DATE NOT NULL,
    inspector_name VARCHAR(150),
    billed_to_customer BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
);

-- Maintenance Table (SRS Section 6)
CREATE TABLE IF NOT EXISTS maintenance (
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    last_service_date DATE,
    next_service_date DATE NOT NULL,
    maintenance_status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, IN_PROGRESS, COMPLETED
    description VARCHAR(500),
    service_cost DOUBLE DEFAULT 0.0,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
);

-- Review Table (SRS Section 3.1 FR12)
CREATE TABLE IF NOT EXISTS review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    booking_id INT,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
);
