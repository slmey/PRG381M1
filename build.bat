@echo off
REM Build script for BC Student Wellness Management System
REM Run this script to compile and package the application

echo ========================================
echo BC Student Wellness Management System
echo Build Script
echo ========================================

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Clean and compile
echo.
echo [1/4] Cleaning previous builds...
call mvn clean

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

echo.
echo [2/4] Compiling source code...
call mvn compile

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo [3/4] Running tests...
call mvn test

echo.
echo [4/4] Packaging application...
call mvn package

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Packaging failed
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo WAR file created: target\bc-student-wellness.war
echo.
echo To deploy:
echo 1. Copy the WAR file to your Tomcat webapps directory
echo 2. Start Tomcat
echo 3. Access: http://localhost:8080/bc-student-wellness/login
echo.
echo Test accounts:
echo - ST001 / john.doe@student.bc.ac.za / Password123!
echo - ST002 / jane.smith@student.bc.ac.za / Password123!
echo.
pause
