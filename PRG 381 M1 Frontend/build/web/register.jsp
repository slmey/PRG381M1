<%-- 
    Document   : register
    Created on : 14 Jul 2025, 19:44:32
    Author     : debee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>BC Wellness | Register</title>
    <link rel="stylesheet" href="style.css">
    
  </head>
  <body>
    <section class="register-section">
      <div class="register-container">
        <form action="RegisterServlet" method="post">
          <h2>Student Register</h2>

          <div class="register-input">
              <span class="register-icons">
                <ion-icon name="school"></ion-icon>
              </span>
              <input type="text" name="studentNumber" required />
              <label for="studentNumber">Student Number</label>
            </div>

            <div class="register-input">
              <span class="register-icons">
                <ion-icon name="person"></ion-icon>
              </span>
              <input type="text" name="name" required />
              <label for="name">First Name</label>
            </div>

            <div class="register-input">
              <span class="register-icons">
                <ion-icon name="people"></ion-icon>
              </span>
              <input type="text" name="surname" required />
              <label for="surname">Last Name</label>
            </div>

            <div class="register-input">
              <span class="register-icons">
                  <ion-icon name="mail"></ion-icon>
              </span>
              <input type="email" name="email" required />
              <label for="email">Email</label>
            </div>

            <div class="register-input">
              <span class="register-icons">
                <ion-icon name="phone-portrait"></ion-icon>
              </span>
              <input type="tel" name="phone" required />
              <label for="phone">Phone Number</label>
            </div>

            <div class="register-input">
              <span class="register-icons">
                <ion-icon name="lock"></ion-icon>
              </span>
              <input type="password" name="password" required />
              <label for="password">Password</label>
            </div>

            <button id="register-btn" type="submit">register</button>

            <div class="login-link">
              <p>Already have an account? <a href="login.jsp">Log In</a></p>
              <p>Back to <a href="index.jsp">Home</a></p>
            </div>
        </form>
      </div>

        

        <script src="https://unpkg.com/ionicons@4.5.10-0/dist/ionicons.js"></script>
      </section>
    </div>
  </body>
</html>
