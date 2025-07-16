# BC Student Wellness Management System

A web application for managing student wellness services built with Java servlets, JSP, and Maven.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Building](#building)
- [Testing](#testing)
- [Database Setup](#database-setup)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java 11 or higher** - [Download Java JDK](https://adoptium.net/)
- **Apache Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Git** - [Download Git](https://git-scm.com/)

### Verify Installation

```bash
java -version
mvn -version
git --version
```

## Installation

1. **Clone the repository:**
   ```bash
   git clone <your-repository-url>
   cd PRG381M1
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

## Configuration

### Database Configuration

The application supports two database configurations:

1. **H2 Database (Development/Testing)** - Embedded database that requires no additional setup
2. **PostgreSQL (Production)** - External database server

**Current Configuration**: The application is configured to use H2 database by default.

#### Switching Between Databases

To switch between H2 and PostgreSQL, modify the `FORCE_H2_DATABASE` constant in `src/main/java/com/bcwellness/utils/DatabaseConnection.java`:

- **H2 Mode (Current)**: `FORCE_H2_DATABASE = true`
- **PostgreSQL Mode**: `FORCE_H2_DATABASE = false`

#### PostgreSQL Setup (For Production)

When ready to use PostgreSQL:

1. Set `FORCE_H2_DATABASE = false` in `DatabaseConnection.java`
2. Update PostgreSQL connection details in the same file:
   - Database URL
   - Username
   - Password
3. Ensure PostgreSQL server is running
4. Create the database specified in the connection string

### Environment Setup

The application will use H2 database by default for development. No additional configuration is required for basic functionality.

## Running the Application

### Option 1: Using Jetty Plugin (Recommended)

```bash
mvn jetty:run
```

The application will be available at: `http://localhost:8081/bc-wellness`

### Option 2: Using Tomcat Plugin

```bash
mvn tomcat7:run
```

The application will be available at: `http://localhost:8081/bc-wellness`

### Option 3: Deploy to External Server

1. Build the WAR file:
   ```bash
   mvn clean package
   ```

2. Deploy the generated `target/bc-student-wellness.war` to your application server.

## Building

### Clean and Build
```bash
mvn clean compile
```

### Package as WAR
```bash
mvn clean package
```

### Skip Tests During Build
```bash
mvn clean package -DskipTests
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Database Tests
```bash
mvn exec:java -Dexec.mainClass="com.bcwellness.utils.DatabaseTest"
```

## Database Setup

### H2 Database (Default)
- Automatically configured for development
- Database files are excluded from version control
- No additional setup required

### PostgreSQL Setup
1. Install PostgreSQL server
2. Create a database named `bc_wellness`
3. Update connection settings in your configuration files
4. Run database migration scripts (if any)

## Project Structure

```
PRG381M1/
├── src/
│   ├── main/
│   │   ├── java/           # Java source code
│   │   ├── resources/      # Configuration files
│   │   └── webapp/         # Web resources (JSP, CSS, JS)
│   └── test/
│       └── java/           # Test files
├── target/                 # Build output (auto-generated)
├── lib/                    # External libraries (if any)
├── pom.xml                 # Maven configuration
└── README.md              # This file
```

## Dependencies

### Core Dependencies
- **Java Servlet API 4.0.1** - Web servlet support
- **JSP API 2.3.3** - JavaServer Pages support
- **JSTL 1.2** - JSP Standard Tag Library
- **PostgreSQL JDBC 42.6.0** - PostgreSQL database driver
- **H2 Database 2.2.224** - Embedded database for development
- **C3P0 0.9.5.5** - Connection pooling
- **SLF4J & Logback** - Logging framework
- **Jackson 2.15.2** - JSON processing
- **JUnit 4.13.2** - Testing framework

### Maven Plugins
- **Maven Compiler Plugin 3.11.0** - Java compilation
- **Maven WAR Plugin 3.3.2** - WAR packaging
- **Tomcat7 Maven Plugin 2.2** - Development server
- **Jetty Maven Plugin 9.4.53** - Development server
- **Exec Maven Plugin 3.5.1** - Running Java applications

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Change the port in `pom.xml` under the Jetty/Tomcat plugin configuration
   - Or kill the process using the port: `netstat -ano | findstr :8081`

2. **Java Version Issues**
   - Ensure Java 11+ is installed and `JAVA_HOME` is set correctly
   - Check with: `echo $JAVA_HOME` (Linux/Mac) or `echo %JAVA_HOME%` (Windows)

3. **Maven Build Failures**
   - Clear Maven cache: `mvn clean`
   - Update dependencies: `mvn dependency:resolve`
   - Check internet connection for dependency downloads

4. **Database Connection Issues**
   - Verify database configuration in resources
   - Check if PostgreSQL service is running (if using PostgreSQL)
   - H2 database issues usually resolve with `mvn clean`

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Review the `logs/` directory for application logs
3. Verify all prerequisites are correctly installed
4. Ensure no firewall is blocking the application port

## Development Notes

- The application uses Maven for dependency management
- JSP files are located in `src/main/webapp/`
- Java classes follow the package structure under `src/main/java/`
- Database configurations are environment-specific and not committed to version control
- The application is configured to run on port 8081 by default

## License

This project is part of an academic assignment for PRG381M1.
