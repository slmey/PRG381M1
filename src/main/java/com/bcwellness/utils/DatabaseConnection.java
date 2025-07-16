package com.bcwellness.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database Connection utility that switches between production and development databases
 * 
 * CONFIGURATION:
 * - Set FORCE_H2_DATABASE = true to always use H2 (development mode)
 * - Set FORCE_H2_DATABASE = false to use PostgreSQL when available
 * 
 * SWITCHING TO POSTGRESQL:
 * 1. Change FORCE_H2_DATABASE to false
 * 2. Update PostgreSQL connection details in getConnection() method
 * 3. Ensure PostgreSQL server is running and accessible
 * 4. Update database name, username, and password as needed
 * 
 * The PostgreSQL code is preserved and ready for production use.
 */
public class DatabaseConnection {
    
    // Force H2 database usage for development (set to true to use H2, false to use PostgreSQL)
    private static final boolean FORCE_H2_DATABASE = true;
    
    // Check if we're in development mode (no PostgreSQL available)
    private static final boolean DEVELOPMENT_MODE = FORCE_H2_DATABASE || isDevelopmentMode();
    
    private static boolean isDevelopmentMode() {
        // If forced to use H2, skip PostgreSQL check
        if (FORCE_H2_DATABASE) {
            System.out.println("🔧 Forced H2 mode - using H2 development database");
            return true;
        }
        
        try {
            Class.forName("org.postgresql.Driver");
            // Try to connect to PostgreSQL
            Connection testConn = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bc_wellness_db", "postgres", "password");
            testConn.close();
            System.out.println("🗄️ PostgreSQL detected - using production database");
            return false; // PostgreSQL is available
        } catch (Exception e) {
            System.out.println("🔧 PostgreSQL not available - using H2 development database");
            System.out.println("   Error: " + e.getMessage());
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
