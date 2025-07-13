package com.bcwellness.servlets;

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


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in using SessionManager service
        if (ServiceFactory.getSessionManager().redirectIfLoggedIn(request, response)) {
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    
     //Handle POST requests - process login using modular services
     
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get all required services from factory
        ValidationService validator = ServiceFactory.getValidationService();
        SecurityService security = ServiceFactory.getSecurityService();
        AuthenticationService auth = ServiceFactory.getAuthenticationService();
        SessionManager session = ServiceFactory.getSessionManager();
        
        // Extract and clean form parameters
        String loginId = security.cleanLoginIdentifier(request.getParameter("loginIdentifier"));
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Clear any existing messages
        request.removeAttribute("errorMessage");
        request.removeAttribute("successMessage");
        
        // Validate input using ValidationService
        ValidationService.ValidationResult validation = validator.validateLoginInput(loginId, password);
        if (!validation.isValid()) {
            setErrorAndForward(request, response, validation.getMessage(), loginId);
            return;
        }
        
        // Log security event
        security.logSecurityEvent("Login attempt for: " + loginId, request);
        
        try {
            // Authenticate using AuthenticationService
            AuthenticationService.AuthenticationResult authResult = auth.authenticateUser(loginId, password);
            
            if (authResult.isSuccessful()) {
                // Success - create session using SessionManager
                boolean remember = "on".equals(rememberMe) || "true".equals(rememberMe);
                session.createUserSession(request, authResult.getUser(), remember);
                
                security.logSecurityEvent("Successful login: " + authResult.getUser().getEmail(), request);
                
                // Handle redirect using SecurityService
                String redirect = request.getParameter("redirect");
                if (redirect != null && security.isValidRedirectUrl(redirect, request.getContextPath())) {
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                }
            } else {
                // Failed authentication - track using SessionManager and SecurityService
                int attempts = session.trackFailedLogin(request);
                String errorMsg = security.generateFailedLoginMessage(attempts);
                
                security.logSecurityEvent("Failed login: " + loginId + " (attempt " + attempts + ")", request);
                
                setErrorAndForward(request, response, errorMsg, loginId);
            }
            
        } catch (Exception e) {
            // Handle errors
            System.err.println("Login system error: " + e.getMessage());
            e.printStackTrace();
            
            security.logSecurityEvent("System error during login for: " + loginId, request);
            setErrorAndForward(request, response, 
                "A system error occurred. Please try again later or contact support.", loginId);
        }
    }
    
    
     //Helper method to set error and forward to login page
     
    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, 
            String errorMessage, String loginIdentifier) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    
     //Public static method for handling logout (maintains backward compatibility)
     
    public static void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        ServiceFactory.getSessionManager().logoutUser(request, response, true);
    }
}
