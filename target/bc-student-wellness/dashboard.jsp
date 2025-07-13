<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.bcwellness.utils.SessionUtils" %>

<!-- Check if user is logged in -->
<%
    if (!SessionUtils.isUserLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - BC Student Wellness Management System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f8f9fa;
            line-height: 1.6;
        }
        
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem 0;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .logo h1 {
            font-size: 24px;
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        
        .user-details {
            text-align: right;
        }
        
        .user-name {
            font-weight: 600;
            font-size: 16px;
        }
        
        .user-role {
            font-size: 14px;
            opacity: 0.9;
        }
        
        .logout-btn {
            background: rgba(255,255,255,0.2);
            color: white;
            border: 1px solid rgba(255,255,255,0.3);
            padding: 8px 16px;
            border-radius: 4px;
            text-decoration: none;
            transition: background 0.3s ease;
        }
        
        .logout-btn:hover {
            background: rgba(255,255,255,0.3);
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 30px 20px;
        }
        
        .welcome-section {
            background: white;
            border-radius: 10px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        
        .welcome-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 10px;
        }
        
        .welcome-subtitle {
            color: #666;
            font-size: 16px;
            margin-bottom: 20px;
        }
        
        .session-info {
            background: #e8f4f8;
            padding: 15px;
            border-radius: 6px;
            margin-top: 20px;
        }
        
        .session-info h4 {
            color: #2c3e50;
            margin-bottom: 10px;
        }
        
        .session-info p {
            color: #34495e;
            margin: 5px 0;
        }
        
        .services-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        
        .service-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        .service-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        
        .service-icon {
            font-size: 48px;
            margin-bottom: 15px;
            display: block;
        }
        
        .service-card h3 {
            color: #333;
            margin-bottom: 10px;
            font-size: 20px;
        }
        
        .service-card p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .service-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            display: inline-block;
            transition: transform 0.2s ease;
        }
        
        .service-btn:hover {
            transform: translateY(-2px);
        }
        
        .quick-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin: 30px 0;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        
        .stat-number {
            font-size: 32px;
            font-weight: bold;
            color: #667eea;
            display: block;
        }
        
        .stat-label {
            color: #666;
            margin-top: 5px;
        }
        
        @media (max-width: 768px) {
            .header-content {
                flex-direction: column;
                gap: 10px;
            }
            
            .user-info {
                flex-direction: column;
                gap: 10px;
            }
            
            .services-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="header-content">
            <div class="logo">
                <h1>BC Wellness System</h1>
            </div>
            <div class="user-info">
                <div class="user-details">
                    <div class="user-name">Welcome, <%= SessionUtils.getUserFullName(request) %></div>
                    <div class="user-role">Student ID: <%= SessionUtils.getStudentNumber(request) %></div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
            </div>
        </div>
    </header>
    
    <div class="container">
        <div class="welcome-section">
            <h2 class="welcome-title">Welcome to Your Wellness Dashboard</h2>
            <p class="welcome-subtitle">
                Access all your wellness services and manage your appointments in one place.
            </p>
            
            <div class="session-info">
                <h4>Session Information</h4>
                <p><strong>Login Time:</strong> <%= new java.util.Date(SessionUtils.getSessionCreationTime(request)) %></p>
                <p><strong>Session Duration:</strong> <%= SessionUtils.getSessionDurationMinutes(request) %> minutes</p>
                <p><strong>Email:</strong> <%= request.getSession().getAttribute("userEmail") %></p>
            </div>
        </div>
        
        <div class="quick-stats">
            <div class="stat-card">
                <span class="stat-number">3</span>
                <div class="stat-label">Upcoming Appointments</div>
            </div>
            <div class="stat-card">
                <span class="stat-number">12</span>
                <div class="stat-label">Available Counselors</div>
            </div>
            <div class="stat-card">
                <span class="stat-number">5</span>
                <div class="stat-label">Feedback Submitted</div>
            </div>
            <div class="stat-card">
                <span class="stat-number">24/7</span>
                <div class="stat-label">Support Available</div>
            </div>
        </div>
        
        <div class="services-grid">
            <div class="service-card">
                <span class="service-icon">📅</span>
                <h3>Appointment Management</h3>
                <p>Book, view, and manage your counseling appointments with our qualified professionals.</p>
                <a href="#appointments" class="service-btn">Manage Appointments</a>
            </div>
            
            <div class="service-card">
                <span class="service-icon">👥</span>
                <h3>Find Counselors</h3>
                <p>Browse our team of experienced counselors and find the right match for your needs.</p>
                <a href="#counselors" class="service-btn">Browse Counselors</a>
            </div>
            
            <div class="service-card">
                <span class="service-icon">💬</span>
                <h3>Feedback & Reviews</h3>
                <p>Share your experience and help us improve our services for all students.</p>
                <a href="#feedback" class="service-btn">Give Feedback</a>
            </div>
            
            <div class="service-card">
                <span class="service-icon">📚</span>
                <h3>Wellness Resources</h3>
                <p>Access helpful articles, guides, and resources for your mental health journey.</p>
                <a href="#resources" class="service-btn">View Resources</a>
            </div>
            
            <div class="service-card">
                <span class="service-icon">🔔</span>
                <h3>Notifications</h3>
                <p>Stay updated with appointment reminders and important wellness announcements.</p>
                <a href="#notifications" class="service-btn">View Notifications</a>
            </div>
            
            <div class="service-card">
                <span class="service-icon">⚙️</span>
                <h3>Account Settings</h3>
                <p>Update your profile information, preferences, and account security settings.</p>
                <a href="#settings" class="service-btn">Manage Account</a>
            </div>
        </div>
    </div>
    
    <script>
        // Auto-refresh session info every minute
        setInterval(function() {
            // Update session duration display
            const duration = document.querySelector('.session-info p:nth-child(3)');
            if (duration) {
                const currentMinutes = parseInt(duration.textContent.match(/\d+/)[0]);
                duration.innerHTML = '<strong>Session Duration:</strong> ' + (currentMinutes + 1) + ' minutes';
            }
        }, 60000);
        
        // Add click handlers for service cards
        document.querySelectorAll('.service-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                const service = this.getAttribute('href').substring(1);
                alert('Navigating to ' + service + ' section...\n\nThis functionality will be implemented in Milestone 2.');
            });
        });
    </script>
</body>
</html>
