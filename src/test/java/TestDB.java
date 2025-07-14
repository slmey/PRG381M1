package com.bcwellness.tests;

import com.bcwellness.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {

    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful.");
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace(); // Helpful for debugging
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
