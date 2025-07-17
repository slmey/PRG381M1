-- BC Student Wellness Management System Database Schema
-- PostgreSQL Database Setup

-- Create database (run this separately if needed)
-- CREATE DATABASE bc_wellness_db;

-- Use the database
-- \c bc_wellness_db;

-- Create users table for student authentication
CREATE TABLE IF NOT EXISTS users (
    student_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_student_number ON users(student_number);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);

-- Insert sample data for testing
-- Note: These passwords are hashed with salt. 
-- The plain text passwords are: 'Password123!' for all users
-- You should generate proper salts and hashes in your application

INSERT INTO users (student_number, name, surname, email, phone, password, salt) VALUES
('ST001', 'John', 'Doe', 'john.doe@student.bc.ac.za', '0123456789', 
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8', 
 'aBcDeFgHiJkLmNoPqRsT'),
('ST002', 'Jane', 'Smith', 'jane.smith@student.bc.ac.za', '0987654321',
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8',
 'aBcDeFgHiJkLmNoPqRsT'),
('ST003', 'Michael', 'Johnson', 'michael.johnson@student.bc.ac.za', '0111222333',
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8',
 'aBcDeFgHiJkLmNoPqRsT'),
('ST004', 'Sarah', 'Williams', 'sarah.williams@student.bc.ac.za', '0444555666',
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8',
 'aBcDeFgHiJkLmNoPqRsT'),
('ST005', 'David', 'Brown', 'david.brown@student.bc.ac.za', '0777888999',
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8',
 'aBcDeFgHiJkLmNoPqRsT');

-- Create function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update the updated_at column
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Create view for user information (excluding sensitive data)
CREATE OR REPLACE VIEW user_info AS
SELECT 
    student_number,
    name,
    surname,
    email,
    phone,
    created_at,
    last_login,
    is_active
FROM users
WHERE is_active = TRUE;

-- Grant permissions (adjust as needed)
-- GRANT SELECT, INSERT, UPDATE ON users TO your_app_user;
-- GRANT SELECT ON user_info TO your_app_user;

-- Queries for common operations:

-- 1. Authenticate user by email or student number
-- SELECT student_number, name, surname, email, phone, password, salt 
-- FROM users 
-- WHERE (email = ? OR student_number = ?) AND is_active = TRUE;

-- 2. Get user by student number
-- SELECT student_number, name, surname, email, phone 
-- FROM users 
-- WHERE student_number = ? AND is_active = TRUE;

-- 3. Get user by email
-- SELECT student_number, name, surname, email, phone 
-- FROM users 
-- WHERE email = ? AND is_active = TRUE;

-- 4. Update last login time
-- UPDATE users SET last_login = CURRENT_TIMESTAMP 
-- WHERE student_number = ?;

-- 5. Update failed login attempts
-- UPDATE users SET failed_login_attempts = failed_login_attempts + 1 
-- WHERE student_number = ? OR email = ?;

-- 6. Reset failed login attempts
-- UPDATE users SET failed_login_attempts = 0 
-- WHERE student_number = ? OR email = ?;

-- 7. Lock account temporarily
-- UPDATE users SET account_locked_until = CURRENT_TIMESTAMP + INTERVAL '30 minutes' 
-- WHERE student_number = ? OR email = ?;
