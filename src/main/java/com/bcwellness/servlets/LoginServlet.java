package com.bcwellness.servlets;

import com.bcwellness.dao.UserDAO;
import com.bcwellness.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet handles user authentication for the BC Student Wellness Management System
 * Supports login via email or student number
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
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
        
        // Get form parameters
        String loginIdentifier = request.getParameter("loginIdentifier");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Clear any existing error messages
        request.removeAttribute("errorMessage");
        request.removeAttribute("successMessage");
        
        // Validate input
        String validationError = validateLoginInput(loginIdentifier, password);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.setAttribute("loginIdentifier", loginIdentifier);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Authenticate user
            User user = userDAO.authenticateUser(loginIdentifier.trim(), password);
            
            if (user != null) {
                // Authentication successful
                handleSuccessfulLogin(request, response, user, rememberMe);
            } else {
                // Authentication failed
                handleFailedLogin(request, response, loginIdentifier);
            }
            
        } catch (Exception e) {
            // Handle database or other errors
            System.err.println("Error during login process: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("errorMessage", 
                "A system error occurred. Please try again later or contact support.");
            request.setAttribute("loginIdentifier", loginIdentifier);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Validate login input parameters
     */
    private String validateLoginInput(String loginIdentifier, String password) {
        if (loginIdentifier == null || loginIdentifier.trim().isEmpty()) {
            return "Please enter your email or student number.";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Please enter your password.";
        }
        
        // Check minimum length requirements
        if (loginIdentifier.trim().length() < 3) {
            return "Email or student number must be at least 3 characters long.";
        }
        
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        
        return null; // No validation errors
    }
    
    /**
     * Handle successful login
     */
    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, 
            User user, String rememberMe) throws IOException {
        
        // Create or get existing session
        HttpSession session = request.getSession(true);
        
        // Store user information in session
        session.setAttribute("user", user);
        session.setAttribute("studentNumber", user.getStudentNumber());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("loginTime", System.currentTimeMillis());
        
        // Set session timeout (30 minutes)
        session.setMaxInactiveInterval(30 * 60);
        
        // Handle "Remember Me" functionality
        if ("on".equals(rememberMe) || "true".equals(rememberMe)) {
            // Extend session timeout to 7 days for remember me
            session.setMaxInactiveInterval(7 * 24 * 60 * 60);
            session.setAttribute("rememberMe", true);
        }
        
        // Log successful login
        System.out.println("Successful login for user: " + user.getEmail() + 
                          " (Student: " + user.getStudentNumber() + ")");
        
        // Check for redirect parameter (for deep linking)
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.trim().isEmpty() && 
            isValidRedirectUrl(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else {
            // Default redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        }
    }
    
    /**
     * Handle failed login
     */
    private void handleFailedLogin(HttpServletRequest request, HttpServletResponse response, 
            String loginIdentifier) throws ServletException, IOException {
        
        // Track failed login attempts in session
        HttpSession session = request.getSession(true);
        Integer failedAttempts = (Integer) session.getAttribute("failedLoginAttempts");
        if (failedAttempts == null) {
            failedAttempts = 0;
        }
        failedAttempts++;
        session.setAttribute("failedLoginAttempts", failedAttempts);
        
        // Log failed login attempt
        System.out.println("Failed login attempt for identifier: " + loginIdentifier + 
                          " (Attempt #" + failedAttempts + ")");
        
        // Set error message based on number of attempts
        String errorMessage;
        if (failedAttempts >= 3) {
            errorMessage = "Multiple failed login attempts detected. Please check your credentials carefully. " +
                          "If you continue to have trouble, contact support.";
            // Could implement account lockout here
        } else {
            errorMessage = "Invalid email/student number or password. Please try again.";
        }
        
        // Set attributes for the login page
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("loginIdentifier", loginIdentifier);
        request.setAttribute("failedAttempts", failedAttempts);
        
        // Forward back to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Validate redirect URL to prevent open redirect attacks
     */
    private boolean isValidRedirectUrl(String redirectUrl) {
        // Only allow relative URLs or URLs within the same domain
        return redirectUrl.startsWith("/") && !redirectUrl.startsWith("//") ||
               redirectUrl.startsWith(getServletContext().getContextPath());
    }
    
    /**
     * Handle logout functionality
     */
    public static void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Log logout
            User user = (User) session.getAttribute("user");
            if (user != null) {
                System.out.println("User logged out: " + user.getEmail());
            }
            
            // Invalidate session
            session.invalidate();
        }
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login.jsp?message=logout");
    }
}
