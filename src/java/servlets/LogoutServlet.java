package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LogoutServlet handles user logout functionality
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    
     //Handle both GET and POST requests for logout
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    /**
     * Process logout request
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Log the logout action
            Object userObj = session.getAttribute("user");
            if (userObj != null) {
                System.out.println("User logging out: " + session.getAttribute("userEmail"));
            }
            
            // Invalidate the session
            session.invalidate();
        }
        
        // Set cache control headers to prevent back button access
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Redirect to login page with logout message
        response.sendRedirect(request.getContextPath() + "/login?message=logout");
    }
}
