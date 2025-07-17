package dao;

import models.User;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User operations
 */
public class UserDAO {
    
    private static final String AUTHENTICATE_USER_SQL = 
        "SELECT student_number, first_name, last_name, email, password_hash, password_salt FROM users WHERE email = ? OR student_number = ?";
    
    private static final String GET_USER_BY_EMAIL_SQL = 
        "SELECT student_number, first_name, last_name, email FROM users WHERE email = ?";
    
    private static final String GET_USER_BY_STUDENT_NUMBER_SQL = 
        "SELECT student_number, first_name, last_name, email FROM users WHERE student_number = ?";
    
    /**
     * Authenticate user login credentials
     * @param loginIdentifier Email or student number
     * @param password Plain text password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateUser(String loginIdentifier, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(AUTHENTICATE_USER_SQL);
            statement.setString(1, loginIdentifier);
            statement.setString(2, loginIdentifier);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password_hash");
                
                // Simple password comparison for school assignment
                if (password.equals(storedPassword)) {
                    User user = new User();
                    user.setStudentNumber(resultSet.getString("student_number"));
                    user.setName(resultSet.getString("first_name"));
                    user.setSurname(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Get user by email
     * @param email User email
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(GET_USER_BY_EMAIL_SQL);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                User user = new User();
                user.setStudentNumber(resultSet.getString("student_number"));
                user.setName(resultSet.getString("first_name"));
                user.setSurname(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Get user by student number
     * @param studentNumber Student number
     * @return User object if found, null otherwise
     */
    public User getUserByStudentNumber(String studentNumber) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(GET_USER_BY_STUDENT_NUMBER_SQL);
            statement.setString(1, studentNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                User user = new User();
                user.setStudentNumber(resultSet.getString("student_number"));
                user.setName(resultSet.getString("first_name"));
                user.setSurname(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by student number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Check if user exists by email or student number
     * @param email User email
     * @param studentNumber Student number
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String email, String studentNumber) {
        return getUserByEmail(email) != null || getUserByStudentNumber(studentNumber) != null;
    }
    
    /**
     * Close database resources
     */
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
        
//        DatabaseConnection.closeConnection(connection);
    }
}
