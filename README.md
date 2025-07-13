# BC Student Wellness Management System - PRG381M1

## Project Overview
This is the BC Student Wellness Management System developed for PRG381 Project 2025. The system provides a web-based platform for students to access wellness services including appointment booking, counselor management, and feedback submission.

## Milestone 1: Web Application - Login & Registration System

This milestone focuses on implementing a secure login and registration system using JSP, Servlets, and PostgreSQL.

### Features Implemented

#### 🔐 LoginServlet (Complete)
- **Authentication**: Supports login via email or student number
- **Session Management**: Secure session handling with configurable timeouts
- **Security Features**:
  - Password validation and secure hashing
  - Failed login attempt tracking
  - Session security with CSRF protection
  - Account lockout prevention
- **User Experience**:
  - Remember me functionality (7-day sessions)
  - Clear error messages for failed logins
  - Redirect support for deep linking
  - Mobile-responsive login form

#### 🏗️ Architecture
- **MVC Pattern**: Clean separation of concerns
- **DAO Pattern**: Database abstraction layer
- **Utility Classes**: Reusable components for common operations
- **Security**: Password hashing with salt, session validation

### Project Structure
```
PRG381M1/
├── src/main/java/com/bcwellness/
│   ├── servlets/
│   │   ├── LoginServlet.java      ✅ Complete
│   │   └── LogoutServlet.java     ✅ Complete
│   ├── dao/
│   │   └── UserDAO.java           ✅ Complete
│   ├── models/
│   │   └── User.java              ✅ Complete
│   └── utils/
│       ├── DatabaseConnection.java ✅ Complete
│       ├── PasswordUtils.java     ✅ Complete
│       └── SessionUtils.java      ✅ Complete
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml               ✅ Complete
│   ├── login.jsp                 ✅ Complete
│   └── dashboard.jsp             ✅ Complete
├── database/
│   └── schema.sql                ✅ Complete
└── pom.xml                       ✅ Complete
```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- PostgreSQL 12 or higher
- Apache Tomcat 9 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Database Setup
1. Install and start PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE bc_wellness_db;
   ```
3. Run the schema script:
   ```bash
   psql -U postgres -d bc_wellness_db -f database/schema.sql
   ```
4. Update database credentials in `DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/bc_wellness_db";
   private static final String USERNAME = "postgres";
   private static final String PASSWORD = "your_password";
   ```

### Application Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/slmey/PRG381M1.git
   cd PRG381M1
   ```

2. Build the project:
   ```bash
   mvn clean compile
   ```

3. Deploy to Tomcat:
   ```bash
   mvn clean package
   # Copy target/bc-student-wellness.war to Tomcat webapps/
   ```

4. Start Tomcat and access:
   ```
   http://localhost:8080/bc-student-wellness/login
   ```

### Test Accounts
The database schema includes sample test accounts:

| Student Number | Email | Password |
|---------------|-------|----------|
| ST001 | john.doe@student.bc.ac.za | Password123! |
| ST002 | jane.smith@student.bc.ac.za | Password123! |
| ST003 | michael.johnson@student.bc.ac.za | Password123! |

## LoginServlet Features

### 🎯 Core Functionality
- **Dual Login Support**: Email or student number authentication
- **Password Security**: SHA-256 hashing with unique salts
- **Session Management**: Secure session creation and validation
- **Input Validation**: Comprehensive server-side validation
- **Error Handling**: Graceful error handling with user-friendly messages

### 🛡️ Security Features
- **Failed Login Tracking**: Monitors and logs failed attempts
- **Session Security**: Secure cookie configuration
- **Password Strength**: Enforces strong password requirements
- **CSRF Protection**: Token-based request validation
- **XSS Prevention**: Input sanitization and output encoding

### 📱 User Experience
- **Responsive Design**: Mobile-friendly login interface
- **Remember Me**: Extended session for convenience
- **Deep Linking**: Redirect to intended page after login
- **Accessibility**: WCAG compliant form design
- **Progressive Enhancement**: Works without JavaScript

### 🔧 Technical Implementation
```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // GET: Display login form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Check existing session, forward to login.jsp
    }
    
    // POST: Process login form
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Validate input
        // Authenticate user
        // Create session
        // Redirect to dashboard
    }
}
```

## Testing the LoginServlet

### Manual Testing
1. **Valid Login**: Use test accounts to verify successful authentication
2. **Invalid Credentials**: Test error handling for wrong passwords
3. **Session Management**: Verify session creation and timeout
4. **Remember Me**: Test extended session functionality
5. **Security**: Attempt SQL injection and XSS attacks

### Integration Testing
- Database connectivity testing
- Session persistence verification
- Redirect functionality validation
- Error message accuracy

## Next Steps (Milestone 2)
- RegisterServlet implementation
- Desktop application with Java Swing
- CRUD operations for appointments, counselors, feedback
- JavaDB integration for desktop app

## Contributing
This is a group project for PRG381. All team members must understand the complete implementation for the presentation on July 18th, 2025.

## Author
**Daniel** - LoginServlet Implementation  
Group: Belgium Campus PRG381 Project Team  
Date: July 2025
