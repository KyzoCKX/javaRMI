package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreCon {
    // Database connection details
    private static final String url = "jdbc:postgresql://localhost:5432/dcoms"; // Your DB URL
    private static final String user = "postgres"; // DB username
    private static final String password = "postgres"; // DB password

    // Static connection object, shared globally
    private static Connection connection = null;

    // Private constructor to prevent external instantiation
    private PostgreCon() {
        // Private constructor prevents initialization from outside
    }

    // Static method to get the connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection Succeed");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    // Method to close the connection
    public static String closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;  // Reset connection to null after closing
                return "Connection Closed";
            } catch (SQLException e) {
                return "Failed to Close Connection: " + e.getMessage();
            }
        }
        return "No connection to close.";
    }
    public static String testConnection(){
        if(PostgreCon.connection == null){return "failed connection";}
        return "succeed";
    }

    // For testing the connection
//    public static void main(String[] args) {
//        // Access the static connection
//        Connection con = PostgreCon.getConnection();
//
//        // Use the connection for any operations, like a query, then close it
//        System.out.println(PostgreCon.closeConnection());
//    }
}
