package com.bcwellness.controllers;

import com.bcwellness.models.User;
import com.bcwellness.services.AuthenticationService;
import com.bcwellness.services.SecurityService;
import com.bcwellness.services.SessionManager;
import com.bcwellness.services.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Modular Login Controller using service-oriented architecture
 * This demonstrates separation of concerns and reusability
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    // Service dependencies
    private AuthenticationService authenticationService;
    private SessionManager sessionManager;
    private ValidationService validationService;
    private SecurityService securityService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize services
        this.authenticationService = new AuthenticationService();
        this.sessionManager = new SessionManager();
        this.validationService = new ValidationService();
        this.securityService = new SecurityService();
    }
    
    /**
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in and redirect if necessary
        if (sessionManager.redirectIfLoggedIn(request, response)) {
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process login form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Extract form parameters
        String loginIdentifier = request.getParameter("loginIdentifier");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Clean input
        loginIdentifier = securityService.cleanLoginIdentifier(loginIdentifier);
        
        // Clear any existing messages
        clearRequestMessages(request);
        
        // Security checks
        if (securityService.isSuspiciousRequest(request)) {
            securityService.logSecurityEvent("Suspicious login attempt", request);
        }
        
        // Validate input
        ValidationService.ValidationResult validation = 
            validationService.validateLoginInput(loginIdentifier, password);
        
        if (!validation.isValid()) {
            handleValidationError(request, response, validation.getMessage(), loginIdentifier);
            return;
        }
        
        // Check rate limiting
        String clientIP = securityService.getClientIP(request);
        if (securityService.isRateLimited(clientIP)) {
            handleRateLimitExceeded(request, response, loginIdentifier);
            return;
        }
        
        // Attempt authentication
        try {
            AuthenticationService.AuthenticationResult authResult = 
                authenticationService.authenticateUser(loginIdentifier, password);
            
            if (authResult.isSuccessful()) {
                handleSuccessfulLogin(request, response, authResult.getUser(), rememberMe);
            } else {
                handleFailedLogin(request, response, loginIdentifier, authResult.getMessage());
            }
            
        } catch (Exception e) {
            handleSystemError(request, response, loginIdentifier, e);
        }
    }
    
    /**
     * Handle successful login
     */
    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, 
            User user, String rememberMe) throws IOException {
        
        // Create user session
        boolean rememberMeFlag = "on".equals(rememberMe) || "true".equals(rememberMe);
        sessionManager.createUserSession(request, user, rememberMeFlag);
        
        // Log successful login
        securityService.logSecurityEvent("Successful login for user: " + user.getEmail(), request);
        
        // Handle redirect
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && securityService.isValidRedirectUrl(redirectUrl, request.getContextPath())) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        }
    }
    
    /**
     * Handle failed login
     */
    private void handleFailedLogin(HttpServletRequest request, HttpServletResponse response, 
            String loginIdentifier, String authMessage) throws ServletException, IOException {
        
        // Track failed attempt
        int failedAttempts = sessionManager.trackFailedLogin(request);
        
        // Log failed attempt
        securityService.logSecurityEvent("Failed login attempt for: " + loginIdentifier + 
                                        " (Attempt #" + failedAttempts + ")", request);
        
        // Generate appropriate error message
        String errorMessage = securityService.generateFailedLoginMessage(failedAttempts);
        
        // Check if account should be locked
        if (securityService.shouldLockAccount(failedAttempts)) {
            securityService.logSecurityEvent("Account lockout triggered for: " + loginIdentifier, request);
        }
        
        // Set error attributes and forward to login page
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.setAttribute("failedAttempts", failedAttempts);
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle validation errors
     */
    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, 
            String errorMessage, String loginIdentifier) throws ServletException, IOException {
        
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle rate limit exceeded
     */
    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response, 
            String loginIdentifier) throws ServletException, IOException {
        
        securityService.logSecurityEvent("Rate limit exceeded for: " + loginIdentifier, request);
        
        request.setAttribute("errorMessage", "Too many requests. Please try again later.");
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle system errors
     */
    private void handleSystemError(HttpServletRequest request, HttpServletResponse response, 
            String loginIdentifier, Exception e) throws ServletException, IOException {
        
        System.err.println("System error during login: " + e.getMessage());
        e.printStackTrace();
        
        securityService.logSecurityEvent("System error during login for: " + loginIdentifier, request);
        
        request.setAttribute("errorMessage", 
            "A system error occurred. Please try again later or contact support.");
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Clear any existing request messages
     */
    private void clearRequestMessages(HttpServletRequest request) {
        request.removeAttribute("errorMessage");
        request.removeAttribute("successMessage");
    }
    
    /**
     * Public method for handling logout (can be called from other servlets)
     */
    public void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        sessionManager.logoutUser(request, response, true);
    }
}
