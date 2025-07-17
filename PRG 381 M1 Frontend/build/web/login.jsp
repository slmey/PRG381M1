<%-- 
    Document   : login
    Created on : 14 Jul 2025, 19:43:59
    Author     : debee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>BC Wellness | Log In</title>
    <link rel="stylesheet" href="style.css" />
  </head>
  <body>
    <section class="login-section">
      <div class="login-container">
        <form action="LoginServlet" method="post">
          <h2>Student Login</h2>

          <div class="login-input">
            <span class="login-icons">
              <ion-icon name="mail"></ion-icon>
            </span>
            <input type="email" name="Email" required />
            <label for="Email">Email</label>
          </div>

          <div class="login-input">
            <span class="login-icons">
              <ion-icon name="lock"></ion-icon>
            </span>
            <input type="password" name="Password" required />
            <label for="Password">Password</label>
          </div>

          <button id="login-btn" type="submit">Login</button>

          <div class="register-link">
            <p>Don't have an account? <a href="register.jsp">Register</a></p>
            <p>Back to <a href="index.jsp">Home</a></p>
          </div>
        </form>
      </div>

      <script src="https://unpkg.com/ionicons@4.5.10-0/dist/ionicons.js"></script>
    </section>
  </body>
</html>

