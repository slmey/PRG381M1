package com.bcwellness.controllers;

import com.bcwellness.services.AuthenticationService;
import com.bcwellness.services.SecurityService;
import com.bcwellness.services.ServiceFactory;
import com.bcwellness.services.SessionManager;
import com.bcwellness.services.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple Login Controller using ServiceFactory
 * This is the cleanest, most modular version
 */
@WebServlet("/login2")  // Using different URL to avoid conflict
public class SimpleLoginController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        if (ServiceFactory.getSessionManager().redirectIfLoggedIn(request, response)) {
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get services
        ValidationService validator = ServiceFactory.getValidationService();
        SecurityService security = ServiceFactory.getSecurityService();
        AuthenticationService auth = ServiceFactory.getAuthenticationService();
        SessionManager session = ServiceFactory.getSessionManager();
        
        // Get parameters
        String loginId = security.cleanLoginIdentifier(request.getParameter("loginIdentifier"));
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Validate input
        ValidationService.ValidationResult validation = validator.validateLoginInput(loginId, password);
        if (!validation.isValid()) {
            setErrorAndForward(request, response, validation.getMessage(), loginId);
            return;
        }
        
        // Authenticate
        AuthenticationService.AuthenticationResult authResult = auth.authenticateUser(loginId, password);
        
        if (authResult.isSuccessful()) {
            // Success - create session and redirect
            boolean remember = "on".equals(rememberMe) || "true".equals(rememberMe);
            session.createUserSession(request, authResult.getUser(), remember);
            
            security.logSecurityEvent("Successful login: " + authResult.getUser().getEmail(), request);
            
            // Redirect
            String redirect = request.getParameter("redirect");
            if (redirect != null && security.isValidRedirectUrl(redirect, request.getContextPath())) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
            }
        } else {
            // Failed - track attempts and show error
            int attempts = session.trackFailedLogin(request);
            String errorMsg = security.generateFailedLoginMessage(attempts);
            
            security.logSecurityEvent("Failed login: " + loginId + " (attempt " + attempts + ")", request);
            
            setErrorAndForward(request, response, errorMsg, loginId);
        }
    }
    
    /**
     * Helper method to set error and forward to login page
     */
    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, 
            String errorMessage, String loginIdentifier) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Handle logout
     */
    public void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        ServiceFactory.getSessionManager().logoutUser(request, response, true);
    }
}
