<%@ page contentType="text/html" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("studentName") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String name = (String) session.getAttribute("studentName");
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
