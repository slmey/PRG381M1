# Database setup script for BC Student Wellness Management System

# PostgreSQL setup commands
# Run these commands in PostgreSQL command line or pgAdmin

# 1. Create database
CREATE DATABASE bc_wellness_db;

# 2. Connect to the database
\c bc_wellness_db;

# 3. Create the users table with proper constraints
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

# 4. Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_student_number ON users(student_number);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);

# 5. Insert test data
# Note: These are sample accounts for testing
# Password for all accounts: Password123!
# In production, use proper password hashing

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
('ST005', 'Daniel', 'Brown', 'daniel.brown@student.bc.ac.za', '0777888999',
 'h8K9mN2pQ7rS4tU6vW8xY1zA3bC5dE7fG9hI0jK2lM4nO6pQ8rS0tU2vW4xY6zA8',
 'aBcDeFgHiJkLmNoPqRsT');

# 6. Create function for automatic timestamp updates
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

# 7. Create trigger for updated_at
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

# 8. Verify the setup
SELECT COUNT(*) as total_users FROM users;
SELECT student_number, name, surname, email FROM users WHERE is_active = TRUE;

# Setup complete!
# You can now test the LoginServlet with the created accounts.
