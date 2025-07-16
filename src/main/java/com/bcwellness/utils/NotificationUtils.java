package com.bcwellness.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Utility class for managing notifications across the application
 * Supports different notification types: success, error, warning, info
 */
public class NotificationUtils {
    
    // Notification types
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    public static final String INFO = "info";
    
    // Session attribute keys
    private static final String NOTIFICATION_MESSAGE = "notification_message";
    private static final String NOTIFICATION_TYPE = "notification_type";
    
    /**
     * Add a success notification to the session
     */
    public static void addSuccess(HttpServletRequest request, String message) {
        addNotification(request, SUCCESS, message);
    }
    
    /**
     * Add an error notification to the session
     */
    public static void addError(HttpServletRequest request, String message) {
        addNotification(request, ERROR, message);
    }
    
    /**
     * Add a warning notification to the session
     */
    public static void addWarning(HttpServletRequest request, String message) {
        addNotification(request, WARNING, message);
    }
    
    /**
     * Add an info notification to the session
     */
    public static void addInfo(HttpServletRequest request, String message) {
        addNotification(request, INFO, message);
    }
    
    /**
     * Add a notification to the session
     */
    private static void addNotification(HttpServletRequest request, String type, String message) {
        HttpSession session = request.getSession();
        session.setAttribute(NOTIFICATION_MESSAGE, message);
        session.setAttribute(NOTIFICATION_TYPE, type);
    }
    
    /**
     * Get the notification message from session and remove it
     */
    public static String getNotificationMessage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String message = (String) session.getAttribute(NOTIFICATION_MESSAGE);
        if (message != null) {
            session.removeAttribute(NOTIFICATION_MESSAGE);
        }
        return message;
    }
    
    /**
     * Get the notification type from session and remove it
     */
    public static String getNotificationType(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String type = (String) session.getAttribute(NOTIFICATION_TYPE);
        if (type != null) {
            session.removeAttribute(NOTIFICATION_TYPE);
        }
        return type != null ? type : ERROR; // Default to error if no type specified
    }
    
    /**
     * Check if there's a notification in the session
     */
    public static boolean hasNotification(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute(NOTIFICATION_MESSAGE) != null;
    }
    
    /**
     * Clear all notifications from session
     */
    public static void clearNotifications(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(NOTIFICATION_MESSAGE);
        session.removeAttribute(NOTIFICATION_TYPE);
    }
}
