package com.bcwellness.utils;

import com.bcwellness.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Utility class for session management
 */
public class SessionUtils {
    
    public static final String USER_SESSION_KEY = "user";
    public static final String LOGIN_TIME_KEY = "loginTime";
    public static final String STUDENT_NUMBER_KEY = "studentNumber";
    public static final String USER_NAME_KEY = "userName";
    public static final String USER_EMAIL_KEY = "userEmail";
    public static final String REMEMBER_ME_KEY = "rememberMe";
    public static final String FAILED_LOGIN_ATTEMPTS_KEY = "failedLoginAttempts";
    
    /**
     * Check if user is logged in
     * @param request HttpServletRequest
     * @return true if user is logged in, false otherwise
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_SESSION_KEY) != null;
    }
    
    /**
     * Get logged in user from session
     * @param request HttpServletRequest
     * @return User object if logged in, null otherwise
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(USER_SESSION_KEY);
        }
        return null;
    }
    
    /**
     * Get user's full name from session
     * @param request HttpServletRequest
     * @return User's full name or "Guest" if not logged in
     */
    public static String getUserFullName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userName = (String) session.getAttribute(USER_NAME_KEY);
            return userName != null ? userName : "Guest";
        }
        return "Guest";
    }
    
    /**
     * Get user's student number from session
     * @param request HttpServletRequest
     * @return Student number or null if not logged in
     */
    public static String getStudentNumber(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(STUDENT_NUMBER_KEY);
        }
        return null;
    }
    
    /**
     * Check if user session has expired
     * @param request HttpServletRequest
     * @return true if session is expired, false otherwise
     */
    public static boolean isSessionExpired(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return true;
        }
        
        // Check if session is still valid
        try {
            session.getAttribute(USER_SESSION_KEY);
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }
    
    /**
     * Redirect to login page if user is not authenticated
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return true if redirect was performed, false if user is authenticated
     * @throws IOException if redirect fails
     */
    public static boolean redirectIfNotAuthenticated(HttpServletRequest request, 
            HttpServletResponse response) throws IOException {
        
        if (!isUserLoggedIn(request)) {
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();
            
            // Build redirect URL with original request
            String redirectUrl = request.getContextPath() + "/login";
            if (!requestURI.equals(request.getContextPath() + "/login.jsp") && 
                !requestURI.equals(request.getContextPath() + "/login")) {
                
                String originalUrl = requestURI;
                if (queryString != null) {
                    originalUrl += "?" + queryString;
                }
                redirectUrl += "?redirect=" + java.net.URLEncoder.encode(originalUrl, "UTF-8");
            }
            
            response.sendRedirect(redirectUrl);
            return true;
        }
        
        return false;
    }
    
    /**
     * Invalidate user session
     * @param request HttpServletRequest
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Set session attributes for successful login
     * @param session HttpSession
     * @param user User object
     * @param rememberMe whether to remember the user
     */
    public static void setUserSession(HttpSession session, User user, boolean rememberMe) {
        session.setAttribute(USER_SESSION_KEY, user);
        session.setAttribute(STUDENT_NUMBER_KEY, user.getStudentNumber());
        session.setAttribute(USER_NAME_KEY, user.getFullName());
        session.setAttribute(USER_EMAIL_KEY, user.getEmail());
        session.setAttribute(LOGIN_TIME_KEY, System.currentTimeMillis());
        session.setAttribute(REMEMBER_ME_KEY, rememberMe);
        
        // Set session timeout
        if (rememberMe) {
            session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
        } else {
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
        }
        
        // Clear failed login attempts
        session.removeAttribute(FAILED_LOGIN_ATTEMPTS_KEY);
    }
    
    /**
     * Get session creation time
     * @param request HttpServletRequest
     * @return creation time in milliseconds, or 0 if not available
     */
    public static long getSessionCreationTime(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long loginTime = (Long) session.getAttribute(LOGIN_TIME_KEY);
            return loginTime != null ? loginTime : session.getCreationTime();
        }
        return 0;
    }
    
    /**
     * Get session duration in minutes
     * @param request HttpServletRequest
     * @return session duration in minutes, or 0 if not available
     */
    public static long getSessionDurationMinutes(HttpServletRequest request) {
        long creationTime = getSessionCreationTime(request);
        if (creationTime > 0) {
            return (System.currentTimeMillis() - creationTime) / (1000 * 60);
        }
        return 0;
    }
}
