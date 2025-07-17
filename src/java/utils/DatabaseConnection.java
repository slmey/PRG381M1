/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static String url;
    public static String username;
    public static String password;
    public static final String DRIVER = "org.postgresql.Driver";
    
    static {
         try {
            Class.forName(DRIVER);
            DatabaseConnection.initParams("jdbc:postgresql://localhost:5432/Wellness", "postgres", "pass");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("PostgreSQL driver not found", exception);
        }
    }
    
    public static void initParams(String url, String username, String password) {
        DatabaseConnection.url = url;
        DatabaseConnection.username = username;
        DatabaseConnection.password = password;
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.username, DatabaseConnection.password);
    }
}
