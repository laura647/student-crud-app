package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Placeholder database details - to be updated by your colleague
    private static final String URL = "jdbc:mysql://localhost:3306/school_db?useSSL=false";
    private static final String USER = "root"; // Replace with actual MySQL username
    private static final String PASSWORD = "password"; // Replace with actual MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}