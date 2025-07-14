package com.bcwellness.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DevDatabaseConnection {
    
    private static final String DB_URL = "jdbc:h2:mem:bc_wellness_db;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static boolean initialized = false;
    
  
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initializeDatabase();
            initialized = true;
        }
        
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 database driver not found", e);
        }
    }
    
  
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "student_number VARCHAR(20) UNIQUE NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "first_name VARCHAR(50) NOT NULL," +
                "last_name VARCHAR(50) NOT NULL," +
                "password_hash VARCHAR(255) NOT NULL," +
                "password_salt VARCHAR(255) NOT NULL," +
                "is_active BOOLEAN DEFAULT TRUE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            stmt.executeUpdate(createUsersTable);
            
           
            String insertSampleUsers = "INSERT INTO users (student_number, email, first_name, last_name, password_hash, password_salt) VALUES " +
                "('ST001', 'john.doe@student.bc.edu', 'John', 'Doe', 'password123', ''), " +
                "('ST002', 'jane.smith@student.bc.edu', 'Jane', 'Smith', 'password123', ''), " +
                "('ST003', 'mike.wilson@student.bc.edu', 'Mike', 'Wilson', 'password123', '')";
            
            try {
                stmt.executeUpdate(insertSampleUsers);
                System.out.println("Development database initialized with sample data");
                System.out.println("Test users created:");
                System.out.println("   - Student: ST001, Email: john.doe@student.bc.edu, Password: password123");
                System.out.println("   - Student: ST002, Email: jane.smith@student.bc.edu, Password: password123");
                System.out.println("   - Student: ST003, Email: mike.wilson@student.bc.edu, Password: password123");
            } catch (SQLException e) {
                // Data might already exist, that's okay
                System.out.println("Database already contains sample data");
            }
        }
    }
    
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
   
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
