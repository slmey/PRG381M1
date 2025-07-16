<%@ page contentType="text/html" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%@ page import="com.bcwellness.utils.SessionUtils" %>
<%
    // Invalidate the session
    SessionUtils.invalidateSession(request);
    
    // Redirect to login page with logout message
    response.sendRedirect("login.jsp?message=You have been logged out successfully.");
%>
