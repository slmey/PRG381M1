package main.bcwellness.services;

import main.bcwellness.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Service class to manage user sessions
 * This service can be reused across different parts of the application
 */
public class SessionManager {
    
    // Session timeout constants (in seconds)
    public static final int DEFAULT_SESSION_TIMEOUT = 30 * 60; // 30 minutes
    public static final int REMEMBER_ME_TIMEOUT = 7 * 24 * 60 * 60; // 7 days
    
    // Session attribute names
    public static final String USER_ATTRIBUTE = "user";
    public static final String STUDENT_NUMBER_ATTRIBUTE = "studentNumber";
    public static final String USER_NAME_ATTRIBUTE = "userName";
    public static final String USER_EMAIL_ATTRIBUTE = "userEmail";
    public static final String LOGIN_TIME_ATTRIBUTE = "loginTime";
    public static final String REMEMBER_ME_ATTRIBUTE = "rememberMe";
    public static final String FAILED_LOGIN_ATTEMPTS_ATTRIBUTE = "failedLoginAttempts";
    
    /**
     * Create a new user session after successful login
     * @param request HTTP request
     * @param user Authenticated user
     * @param rememberMe Whether to extend session timeout
     * @return Created session
     */
    public HttpSession createUserSession(HttpServletRequest request, User user, boolean rememberMe) {
        HttpSession session = request.getSession(true);
        
        // Store user information in session
        session.setAttribute(USER_ATTRIBUTE, user);
        session.setAttribute(STUDENT_NUMBER_ATTRIBUTE, user.getStudentNumber());
        session.setAttribute(USER_NAME_ATTRIBUTE, user.getFullName());
        session.setAttribute(USER_EMAIL_ATTRIBUTE, user.getEmail());
        session.setAttribute(LOGIN_TIME_ATTRIBUTE, System.currentTimeMillis());
        
        // Set session timeout based on remember me preference
        if (rememberMe) {
            session.setMaxInactiveInterval(REMEMBER_ME_TIMEOUT);
            session.setAttribute(REMEMBER_ME_ATTRIBUTE, true);
        } else {
            session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT);
        }
        
        // Clear any failed login attempts
        session.removeAttribute(FAILED_LOGIN_ATTEMPTS_ATTRIBUTE);
        
        return session;
    }
    
    /**
     * Check if user is logged in
     * @param request HTTP request
     * @return true if user is logged in, false otherwise
     */
    public boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_ATTRIBUTE) != null;
    }
    
    /**
     * Get the current logged-in user
     * @param request HTTP request
     * @return User object if logged in, null otherwise
     */
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(USER_ATTRIBUTE);
        }
        return null;
    }
    
    /**
     * Track failed login attempts
     * @param request HTTP request
     * @return Number of failed attempts
     */
    public int trackFailedLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Integer failedAttempts = (Integer) session.getAttribute(FAILED_LOGIN_ATTEMPTS_ATTRIBUTE);
        if (failedAttempts == null) {
            failedAttempts = 0;
        }
        failedAttempts++;
        session.setAttribute(FAILED_LOGIN_ATTEMPTS_ATTRIBUTE, failedAttempts);
        return failedAttempts;
    }
    
    /**
     * Get number of failed login attempts
     * @param request HTTP request
     * @return Number of failed attempts
     */
    public int getFailedLoginAttempts(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer failedAttempts = (Integer) session.getAttribute(FAILED_LOGIN_ATTEMPTS_ATTRIBUTE);
            return failedAttempts != null ? failedAttempts : 0;
        }
        return 0;
    }
    
    /**
     * Clear failed login attempts
     * @param request HTTP request
     */
    public void clearFailedLoginAttempts(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(FAILED_LOGIN_ATTEMPTS_ATTRIBUTE);
        }
    }
    
    /**
     * Logout user and invalidate session
     * @param request HTTP request
     * @param response HTTP response
     * @param redirectToLogin Whether to redirect to login page
     * @throws IOException If redirect fails
     */
    public void logoutUser(HttpServletRequest request, HttpServletResponse response, boolean redirectToLogin) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Log logout
            User user = (User) session.getAttribute(USER_ATTRIBUTE);
            if (user != null) {
                System.out.println("User logged out: " + user.getEmail());
            }
            
            // Invalidate session
            session.invalidate();
        }
        
        if (redirectToLogin) {
            response.sendRedirect(request.getContextPath() + "/login?message=logout");
        }
    }
    
    /**
     * Redirect user to dashboard if already logged in
     * @param request HTTP request
     * @param response HTTP response
     * @return true if user was redirected, false if not logged in
     * @throws IOException If redirect fails
     */
    public boolean redirectIfLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
            return true;
        }
        return false;
    }
}
