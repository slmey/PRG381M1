<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - BC Student Wellness Management System</title>
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
        }
        
        .login-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            width: 100%;
            max-width: 450px;
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .login-header h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 28px;
        }
        
        .login-header p {
            color: #666;
            font-size: 16px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .form-group input.error {
            border-color: #e74c3c;
        }
        
        .remember-me {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .remember-me input[type="checkbox"] {
            margin-right: 8px;
        }
        
        .login-btn {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
        }
        
        .login-btn:hover {
            transform: translateY(-2px);
        }
        
        .login-btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }
        
        .error-message {
            background: #fee;
            color: #c33;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            border-left: 4px solid #e74c3c;
        }
        
        .success-message {
            background: #efe;
            color: #3c3;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            border-left: 4px solid #27ae60;
        }
        
        .links {
            text-align: center;
            margin-top: 20px;
        }
        
        .links a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
        }
        
        .links a:hover {
            text-decoration: underline;
        }
        
        .links .separator {
            margin: 0 10px;
            color: #ccc;
        }
        
        .form-help {
            font-size: 14px;
            color: #666;
            margin-top: 10px;
        }
        
        @media (max-width: 480px) {
            .login-container {
                margin: 20px;
                padding: 30px 20px;
            }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>Welcome Back</h1>
            <p>BC Student Wellness Management System</p>
        </div>
        
        <!-- Display success message if logout was successful -->
        <c:if test="${param.message == 'logout'}">
            <div class="success-message">
                You have been successfully logged out.
            </div>
        </c:if>
        
        <!-- Display error message if present -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ${errorMessage}
            </div>
        </c:if>
        
        <!-- Display success message if present -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <div class="form-group">
                <label for="loginIdentifier">Email or Student Number</label>
                <input type="text" 
                       id="loginIdentifier" 
                       name="loginIdentifier" 
                       value="${loginIdentifier}" 
                       placeholder="Enter your email or student number"
                       required 
                       autocomplete="username">
                <div class="form-help">
                    You can login using either your email address or student number.
                </div>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="Enter your password"
                       required 
                       autocomplete="current-password">
            </div>
            
            <div class="remember-me">
                <input type="checkbox" id="rememberMe" name="rememberMe" value="true">
                <label for="rememberMe">Remember me for 7 days</label>
            </div>
            
            <!-- Hidden field for redirect URL -->
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="${param.redirect}">
            </c:if>
            
            <button type="submit" class="login-btn" id="loginButton">
                Sign In
            </button>
        </form>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/register">Create Account</a>
            <span class="separator">|</span>
            <a href="${pageContext.request.contextPath}/forgot-password">Forgot Password?</a>
        </div>
        
        <div class="links" style="margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/">← Back to Home</a>
        </div>
    </div>
    
    <script>
        // Form validation and enhancement
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const loginIdentifier = document.getElementById('loginIdentifier').value.trim();
            const password = document.getElementById('password').value;
            const loginButton = document.getElementById('loginButton');
            
            // Clear previous error styling
            document.getElementById('loginIdentifier').classList.remove('error');
            document.getElementById('password').classList.remove('error');
            
            // Basic validation
            let hasError = false;
            
            if (!loginIdentifier) {
                document.getElementById('loginIdentifier').classList.add('error');
                hasError = true;
            }
            
            if (!password) {
                document.getElementById('password').classList.add('error');
                hasError = true;
            }
            
            if (hasError) {
                e.preventDefault();
                return false;
            }
            
            // Disable button to prevent double submission
            loginButton.disabled = true;
            loginButton.textContent = 'Signing In...';
            
            // Re-enable button after 3 seconds in case of network issues
            setTimeout(function() {
                loginButton.disabled = false;
                loginButton.textContent = 'Sign In';
            }, 3000);
        });
        
        // Auto-focus on first empty field
        window.addEventListener('load', function() {
            const loginIdentifier = document.getElementById('loginIdentifier');
            const password = document.getElementById('password');
            
            if (!loginIdentifier.value) {
                loginIdentifier.focus();
            } else if (!password.value) {
                password.focus();
            }
        });
        
        // Clear error styling on input
        document.getElementById('loginIdentifier').addEventListener('input', function() {
            this.classList.remove('error');
        });
        
        document.getElementById('password').addEventListener('input', function() {
            this.classList.remove('error');
        });
    </script>
</body>
</html>
