<%@ page contentType="text/html" %>
<html>
<head><title>Register</title></head>
<body>
<h2>Student Registration</h2>
<form method="post" action="RegisterServlet">
    Student Number: <input type="text" name="student_number" /><br>
    Name: <input type="text" name="name" /><br>
    Surname: <input type="text" name="surname" /><br>
    Email: <input type="text" name="email" /><br>
    Phone: <input type="text" name="phone" /><br>
    Password: <input type="password" name="password" /><br>
    <input type="submit" value="Register" />
</form>
<p style="color:red">${message}</p>
</body>
</html>