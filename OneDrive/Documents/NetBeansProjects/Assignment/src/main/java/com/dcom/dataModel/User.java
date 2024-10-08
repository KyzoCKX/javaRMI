package com.dcom.dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class User implements Serializable{

    private int userId; // Will be auto-generated
    private String pwd;
    private String status;
    private String userType;
    private String email;

    // Constructor for creating a user (userId will be auto-generated)
    public User(String pwd, String status, String userType, String email) {
        this.pwd = pwd;
        this.status = status;
        this.userType = userType;
        this.email = email;
    }

    // Constructor for retrieving a user
    public User(int userId, String pwd, String status, String userType, String email) {
        this.userId = userId;
        this.pwd = pwd;
        this.status = status;
        this.userType = userType;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Method to insert a user into the database
    public static int createUser(Connection con, User user) {
        String insertQuery = "INSERT INTO public.user (pwd, status, userType, email) VALUES (?, ?, ?) RETURNING user_id";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, user.getPwd());
            pstmt.setString(2, user.getStatus());
            pstmt.setString(3, user.getUserType());
            pstmt.setString(4, user.getEmail());


            // Execute the insert and retrieve the generated userId
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Employee.createEmployee(con, rs.getInt("user_id")); // Assuming createEmployee returns the generated employee ID
                return rs.getInt("user_id"); // Return the generated userId
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert user: " + e.getMessage());
        }
        return -1; // Indicate failure to insert
    }

    // Method to retrieve a user from the database by userId
    public static User getUser(Connection con, int userId) {
        String selectQuery = "SELECT * FROM public.user WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("user_id"),
                                rs.getString("pwd"),
                                rs.getString("status"),
                                rs.getString("userType"),
                                rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve user: " + e.getMessage());
        }
        return null; // User not found
    }
    
    public static List<User> getAllUser(Connection con){
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM public.user";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                                rs.getString("pwd"),
                                rs.getString("status"),
                                rs.getString("userType"),                                
                                rs.getString("email"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }

        return userList; // Return the list of Payroll records

    }

    // Method to update a user's information
    public static void updateUser(Connection con, User user) {
        String updateQuery = "UPDATE public.user SET pwd = ?, status = ?, userType = ? WHERE userId = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, user.getPwd());
            pstmt.setString(2, user.getStatus());
            pstmt.setString(3, user.getUserType());
            pstmt.setInt(4, user.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update user: " + e.getMessage());
        }
    }

    // Method to delete a user from the database
    public static void deleteUser(Connection con, int userId) {
        String deleteQuery = "DELETE FROM public.user WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }
    
    public static boolean getUserByUserIdAndPassword(Connection con, int userId, String pwd, String userType){
        String selectQuery = "SELECT * FROM public.user WHERE user_id = ? AND pwd = ? AND userType = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, pwd);
            pstmt.setString(3, userType);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }
        return false;
    }
        public static boolean getUserByEmailAndPassword(Connection con, String email, String pwd, String userType){
        String selectQuery = "SELECT * FROM public.user WHERE user_id = ? AND pwd = ? AND userType = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, pwd);
            pstmt.setString(3, userType);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }
        return false;
    }
}
