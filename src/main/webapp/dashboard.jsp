<%@ page contentType="text/html" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%@ page import="com.bcwellness.utils.SessionUtils" %>
<%
    // Check if user is logged in using SessionUtils
    if (!SessionUtils.isUserLoggedIn(request)) {
        response.sendRedirect("login.jsp");
        return;
    }
    String name = SessionUtils.getUserFullName(request);
%>
<html>
<head><title>Dashboard</title></head>
<body>
    <h2>Welcome, <%= name %>!</h2>
    <form action="logout.jsp" method="post">
        <input type="submit" value="Logout" />
    </form>
</body>
</html>
