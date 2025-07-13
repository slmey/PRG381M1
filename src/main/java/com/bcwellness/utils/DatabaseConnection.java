package com.bcwellness.utils;

import java.sql.Connection;
import java.sql.SQLException;


  //Database Connection utility that switches between production and development databases
 
public class DatabaseConnection {
    
    // Check if we're in development mode (no PostgreSQL available)
    private static final boolean DEVELOPMENT_MODE = isDevelopmentMode();
    
    private static boolean isDevelopmentMode() {
        try {
            Class.forName("org.postgresql.Driver");
            // Try to connect to PostgreSQL
            java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
            return false; // PostgreSQL is available
        } catch (Exception e) {
            return true; // PostgreSQL not available, use H2
        }
    }
    
    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        if (DEVELOPMENT_MODE) {
            System.out.println("🔧 Using H2 development database");
            return DevDatabaseConnection.getConnection();
        } else {
            // Production PostgreSQL connection
            String url = "jdbc:postgresql://localhost:5432/bc_wellness_db";
            String username = "postgres";
            String password = "password"; // Change this to your PostgreSQL password
            
            try {
                Class.forName("org.postgresql.Driver");
                return java.sql.DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL database driver not found", e);
            }
        }
    }
    
    /**
     * Close connection
     */
    public static void closeConnection(Connection connection) {
        if (DEVELOPMENT_MODE) {
            DevDatabaseConnection.closeConnection(connection);
        } else {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        if (DEVELOPMENT_MODE) {
            return DevDatabaseConnection.testConnection();
        } else {
            try (Connection conn = getConnection()) {
                return conn != null && !conn.isClosed();
            } catch (SQLException e) {
                System.err.println("Database connection test failed: " + e.getMessage());
                return false;
            }
        }
    }
}
