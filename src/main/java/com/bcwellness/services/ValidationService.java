package com.bcwellness.services;

import java.util.regex.Pattern;

/**
 * Service class to handle input validation
 * This service can be reused across different forms and inputs
 */
public class ValidationService {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // Student number pattern (assuming format like ST001, ST002, etc.)
    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile(
        "^ST\\d{3,}$"
    );
    
    /**
     * Validate login input parameters
     * @param loginIdentifier Email or student number
     * @param password Password
     * @return Validation result
     */
    public ValidationResult validateLoginInput(String loginIdentifier, String password) {
        // Check for null or empty values
        if (loginIdentifier == null || loginIdentifier.trim().isEmpty()) {
            return new ValidationResult(false, "Please enter your email or student number.");
        }
        
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Please enter your password.");
        }
        
        // Check minimum length requirements
        if (loginIdentifier.trim().length() < 3) {
            return new ValidationResult(false, "Email or student number must be at least 3 characters long.");
        }
        
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long.");
        }
        
        // Validate format of login identifier
        String trimmedIdentifier = loginIdentifier.trim();
        if (!isValidEmail(trimmedIdentifier) && !isValidStudentNumber(trimmedIdentifier)) {
            return new ValidationResult(false, "Please enter a valid email address or student number.");
        }
        
        return new ValidationResult(true, "Validation successful");
    }
    
    /**
     * Check if string is a valid email address
     * @param email Email to validate
     * @return true if valid email, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Check if string is a valid student number
     * @param studentNumber Student number to validate
     * @return true if valid student number, false otherwise
     */
    public boolean isValidStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        return STUDENT_NUMBER_PATTERN.matcher(studentNumber.trim().toUpperCase()).matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @return Validation result with strength assessment
     */
    public ValidationResult validatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty.");
        }
        
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long.");
        }
        
        if (password.length() > 128) {
            return new ValidationResult(false, "Password must be less than 128 characters long.");
        }
        
        // Check for basic password strength (optional for assignment)
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        if (!hasLower || !hasUpper || !hasDigit) {
            return new ValidationResult(true, "Password is weak. Consider using uppercase, lowercase, and numbers.");
        }
        
        return new ValidationResult(true, "Password is strong.");
    }
    
    /**
     * Sanitize input string to prevent basic injection attacks
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.trim()
                   .replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("&", "&amp;");
    }
    
    /**
     * Inner class to represent validation results
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
