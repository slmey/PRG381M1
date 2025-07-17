package services;

import dao.UserDAO;
import models.User;

/**
 * Service class to handle user authentication logic
 * This service can be reused across different authentication scenarios
 */
public class AuthenticationService {
    
    private final UserDAO userDAO;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }
    
    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    /**
     * Authenticate a user with login identifier and password
     * @param loginIdentifier Email or student number
     * @param password Plain text password
     * @return User object if authentication successful, null otherwise
     */
    public AuthenticationResult authenticateUser(String loginIdentifier, String password) {
        try {
            // Sanitize input
            if (loginIdentifier != null) {
                loginIdentifier = loginIdentifier.trim();
            }
            
            // Attempt authentication
            User user = userDAO.authenticateUser(loginIdentifier, password);
            
            if (user != null) {
                System.out.println("Authentication successful for: " + loginIdentifier);
                return new AuthenticationResult(true, user, "Authentication successful");
            } else {
                System.out.println("Authentication failed for: " + loginIdentifier);
                return new AuthenticationResult(false, null, "Invalid credentials");
            }
            
        } catch (Exception e) {
            System.err.println("Authentication error for " + loginIdentifier + ": " + e.getMessage());
            e.printStackTrace();
            return new AuthenticationResult(false, null, "System error during authentication");
        }
    }
    
    /**
     * Check if a user account exists
     * @param loginIdentifier Email or student number
     * @return true if account exists, false otherwise
     */
    public boolean userExists(String loginIdentifier) {
        try {
            // Use authentication method with empty password to check existence
            User user = userDAO.authenticateUser(loginIdentifier.trim(), "");
            return user != null;
        } catch (Exception e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inner class to represent authentication results
     */
    public static class AuthenticationResult {
        private final boolean successful;
        private final User user;
        private final String message;
        
        public AuthenticationResult(boolean successful, User user, String message) {
            this.successful = successful;
            this.user = user;
            this.message = message;
        }
        
        public boolean isSuccessful() {
            return successful;
        }
        
        public User getUser() {
            return user;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
