package com.bcwellness.services;

import javax.servlet.http.HttpServletRequest;

/**
 * Service class to handle security-related functions
 * This service can be reused across different security scenarios
 */
public class SecurityService {
    
    /**
     * Validate redirect URL to prevent open redirect attacks
     * @param redirectUrl URL to validate
     * @param contextPath Application context path
     * @return true if URL is safe to redirect to, false otherwise
     */
    public boolean isValidRedirectUrl(String redirectUrl, String contextPath) {
        if (redirectUrl == null || redirectUrl.trim().isEmpty()) {
            return false;
        }
        
        // Only allow relative URLs or URLs within the same domain
        return (redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) ||
               redirectUrl.startsWith(contextPath);
    }
    
    /**
     * Generate error message based on failed login attempts
     * @param failedAttempts Number of failed attempts
     * @return Appropriate error message
     */
    public String generateFailedLoginMessage(int failedAttempts) {
        if (failedAttempts >= 5) {
            return "Too many failed login attempts. Your account may be temporarily locked. " +
                   "Please contact support if you continue to have trouble.";
        } else if (failedAttempts >= 3) {
            return "Multiple failed login attempts detected. Please check your credentials carefully. " +
                   "If you continue to have trouble, contact support.";
        } else {
            return "Invalid email/student number or password. Please try again.";
        }
    }
    
    /**
     * Check if account should be locked based on failed attempts
     * @param failedAttempts Number of failed attempts
     * @return true if account should be locked, false otherwise
     */
    public boolean shouldLockAccount(int failedAttempts) {
        return failedAttempts >= 5; // Lock after 5 failed attempts
    }
    
    /**
     * Log security event
     * @param event Security event description
     * @param request HTTP request for context
     */
    public void logSecurityEvent(String event, HttpServletRequest request) {
        String clientIP = getClientIP(request);
        String userAgent = request.getHeader("User-Agent");
        
        System.out.println("SECURITY EVENT: " + event + 
                          " | IP: " + clientIP + 
                          " | User-Agent: " + (userAgent != null ? userAgent : "Unknown"));
    }
    
    /**
     * Get client IP address from request
     * @param request HTTP request
     * @return Client IP address
     */
    public String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty() && !"unknown".equalsIgnoreCase(xRealIP)) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * Check if request looks suspicious
     * @param request HTTP request
     * @return true if request seems suspicious, false otherwise
     */
    public boolean isSuspiciousRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        
        // Basic checks for suspicious patterns
        if (userAgent == null || userAgent.toLowerCase().contains("bot") || 
            userAgent.toLowerCase().contains("crawler") || 
            userAgent.toLowerCase().contains("spider")) {
            return true;
        }
        
        // Check for missing common headers
        if (request.getHeader("Accept") == null || 
            request.getHeader("Accept-Language") == null) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Rate limiting check (basic implementation)
     * For a real application, you'd want to use a more sophisticated approach
     * @param clientIP Client IP address
     * @return true if rate limit exceeded, false otherwise
     */
    public boolean isRateLimited(String clientIP) {
        // For assignment purposes, we'll implement a simple check
        // In a real application, you'd use Redis or a similar solution
        
        // This is a simplified implementation - you could expand this
        // to actually track requests per IP over time
        return false; // Allow all requests for now
    }
    
    /**
     * Clean and normalize login identifier
     * @param loginIdentifier Raw login identifier
     * @return Cleaned login identifier
     */
    public String cleanLoginIdentifier(String loginIdentifier) {
        if (loginIdentifier == null) {
            return null;
        }
        
        // Remove whitespace and convert to lowercase for email consistency
        String cleaned = loginIdentifier.trim();
        
        // If it looks like an email, normalize to lowercase
        if (cleaned.contains("@")) {
            cleaned = cleaned.toLowerCase();
        } else {
            // If it's a student number, normalize to uppercase
            cleaned = cleaned.toUpperCase();
        }
        
        return cleaned;
    }
}
