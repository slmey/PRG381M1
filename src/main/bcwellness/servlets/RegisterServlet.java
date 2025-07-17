package main.bcwellness.servlets;

import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/student_wellness";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "yourpassword"; // ⚠️ In production, don't hardcode this

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNumber = request.getParameter("student_number");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        if (studentNumber.isEmpty() || name.isEmpty() || surname.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            Class.forName("org.postgresql.Driver");

            // Check if user exists
            try (PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
                check.setString(1, email);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("message", "Email already exists.");
                        request.getRequestDispatcher("register.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Hash password
            String hashed = hashPassword(password);

            // Insert user
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (student_number, name, surname, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, studentNumber);
                stmt.setString(2, name);
                stmt.setString(3, surname);
                stmt.setString(4, email);
                stmt.setString(5, phone);
                stmt.setString(6, hashed);
                stmt.executeUpdate();
            }

            request.setAttribute("message", "Registration successful! You may now log in.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}