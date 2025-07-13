package com.bcwellness.controllers;

import com.bcwellness.services.ServiceFactory;
import com.bcwellness.services.SessionManager;
import com.bcwellness.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Example servlet showing how to reuse the modular services
 * This demonstrates the reusability of the service-oriented approach
 */
@WebServlet("/user-info")
public class UserInfoController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Reuse SessionManager service
        SessionManager sessionManager = ServiceFactory.getSessionManager();
        
        // Check if user is logged in
        if (!sessionManager.isUserLoggedIn(request)) {
            out.println("<html><body>");
            out.println("<h2>Access Denied</h2>");
            out.println("<p>You must be logged in to view this page.</p>");
            out.println("<a href='" + request.getContextPath() + "/login'>Login</a>");
            out.println("</body></html>");
            return;
        }
        
        // Get current user
        User currentUser = sessionManager.getCurrentUser(request);
        
        // Display user information
        out.println("<html><head><title>User Information</title></head><body>");
        out.println("<h2>User Information</h2>");
        out.println("<table border='1' cellpadding='10'>");
        out.println("<tr><td><strong>Student Number:</strong></td><td>" + currentUser.getStudentNumber() + "</td></tr>");
        out.println("<tr><td><strong>Full Name:</strong></td><td>" + currentUser.getFullName() + "</td></tr>");
        out.println("<tr><td><strong>Email:</strong></td><td>" + currentUser.getEmail() + "</td></tr>");
        out.println("<tr><td><strong>Login Time:</strong></td><td>" + 
                   new java.util.Date((Long) request.getSession().getAttribute("loginTime")) + "</td></tr>");
        out.println("</table>");
        
        out.println("<br><br>");
        out.println("<a href='" + request.getContextPath() + "/dashboard.jsp'>Back to Dashboard</a> | ");
        out.println("<a href='" + request.getContextPath() + "/logout'>Logout</a>");
        out.println("</body></html>");
    }
}
