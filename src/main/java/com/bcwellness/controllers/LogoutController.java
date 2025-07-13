package com.bcwellness.controllers;

import com.bcwellness.services.ServiceFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple logout controller using modular services
 * Shows how easy it is to create new functionality with the service approach
 */
@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Use SessionManager service to handle logout
        ServiceFactory.getSessionManager().logoutUser(request, response, true);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Handle POST requests the same way
        doGet(request, response);
    }
}
