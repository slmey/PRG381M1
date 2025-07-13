package com.bcwellness.services;

/**
 * Service factory to manage service instances
 * This provides a central place to configure and access services
 */
public class ServiceFactory {
    
    // Singleton instances
    private static AuthenticationService authenticationService;
    private static SessionManager sessionManager;
    private static ValidationService validationService;
    private static SecurityService securityService;
    
    /**
     * Get AuthenticationService instance
     * @return AuthenticationService instance
     */
    public static AuthenticationService getAuthenticationService() {
        if (authenticationService == null) {
            authenticationService = new AuthenticationService();
        }
        return authenticationService;
    }
    
    /**
     * Get SessionManager instance
     * @return SessionManager instance
     */
    public static SessionManager getSessionManager() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }
    
    /**
     * Get ValidationService instance
     * @return ValidationService instance
     */
    public static ValidationService getValidationService() {
        if (validationService == null) {
            validationService = new ValidationService();
        }
        return validationService;
    }
    
    /**
     * Get SecurityService instance
     * @return SecurityService instance
     */
    public static SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = new SecurityService();
        }
        return securityService;
    }
    
    /**
     * Reset all services (useful for testing)
     */
    public static void reset() {
        authenticationService = null;
        sessionManager = null;
        validationService = null;
        securityService = null;
    }
}
