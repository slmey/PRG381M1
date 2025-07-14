<%@ page contentType="text/html" %>
<html>
<head><title>Login</title></head>
<body>
<h2>Login</h2>
<form method="post" action="LoginServlet">
    Email: <input type="text" name="email" /><br>
    Password: <input type="password" name="password" /><br>
    <input type="submit" value="Login" />
</form>
<p style="color:red">${message}</p>
</body>
</html>