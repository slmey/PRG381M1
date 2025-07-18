<%@ page contentType="text/html" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BC Student Wellness - Login</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .login-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            position: relative;
            overflow: hidden;
        }

        .login-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #667eea, #764ba2);
        }

        .logo {
            text-align: center;
            margin-bottom: 30px;
        }

        .logo h1 {
            color: #333;
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 5px;
        }

        .logo p {
            color: #666;
            font-size: 14px;
            font-weight: 400;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group input {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
            background-color: #fafafa;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-group input::placeholder {
            color: #aaa;
        }

        .login-button {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .login-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .login-button:active {
            transform: translateY(0);
        }

        .error-message {
            color: #e74c3c;
            background-color: #fdf0ef;
            padding: 12px;
            border-radius: 8px;
            margin-top: 20px;
            font-size: 14px;
            border-left: 4px solid #e74c3c;
        }

        .demo-credentials {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            border-left: 4px solid #28a745;
        }

        .demo-credentials h4 {
            color: #28a745;
            margin-bottom: 10px;
            font-size: 14px;
        }

        .demo-credentials p {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }

        .demo-credentials code {
            background-color: #e9ecef;
            padding: 2px 4px;
            border-radius: 3px;
            font-family: 'Courier New', monospace;
        }

        @media (max-width: 480px) {
            .login-container {
                padding: 30px 20px;
            }
            
            .logo h1 {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <h1>BC Student Wellness</h1>
            <p>Management System</p>
        </div>
        
        <form method="post" action="login">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" value="${email}" required />
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required />
            </div>
            
            <button type="submit" class="login-button">Sign In</button>
        </form>
        
        <% if (request.getAttribute("message") != null && !request.getAttribute("message").toString().isEmpty()) { %>
        <div class="error-message">
            ${message}
        </div>
        <% } %>
        
        <div class="demo-credentials">
            <h4>Demo Credentials</h4>
            <p><strong>Email:</strong> <code>john.doe@student.bc.edu</code></p>
            <p><strong>Email:</strong> <code>jane.smith@student.bc.edu</code></p>
            <p><strong>Email:</strong> <code>mike.wilson@student.bc.edu</code></p>
            <p><strong>Password:</strong> <code>password123</code></p>
        </div>
    </div>
</body>
</html>