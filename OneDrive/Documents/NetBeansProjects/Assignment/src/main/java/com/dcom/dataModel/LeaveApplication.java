package com.dcom.dataModel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class LeaveApplication implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int userId;
    private Date date; // Using java.sql.Date for database compatibility
    private int numberOfDays;
    private String type; // e.g., "annual", "sick", etc.
    private String status; // e.g., "pending", "approved", "rejected"

    // Constructor for creating a new leave application
    public LeaveApplication(int userId, Date date, int numberOfDays, String type, String status) {
        this.userId = userId;
        this.date = date;
        this.numberOfDays = numberOfDays;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to apply for leave (insert into the database)
    public static void applyLeave(Connection con, LeaveApplication leaveApp) {
        String insertQuery = "INSERT INTO leave_application (user_id, date, number_of_days, type, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, leaveApp.getUserId());
            pstmt.setDate(2, leaveApp.getDate());
            pstmt.setInt(3, leaveApp.getNumberOfDays());
            pstmt.setString(4, leaveApp.getType());
            pstmt.setString(5, leaveApp.getStatus());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Leave application submitted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to submit leave application: " + e.getMessage());
        }
    }

    // Method to get leave application by user ID
    public static List<LeaveApplication> getLeaveByUserId(Connection con, int userId) {
            String selectQuery = "SELECT * FROM leave_application WHERE user_id = ?";
            List<LeaveApplication> leaveApplications = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LeaveApplication leaveApplication = new LeaveApplication(
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getInt("number_of_days"),
                        rs.getString("type"),
                        rs.getString("status")
                );
                leaveApplications.add(leaveApplication); // Add to the list
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve leave applications: " + e.getMessage());
        }
        return leaveApplications; // Return the list of applications
    }

    // Method to update leave application status
    public static void updateLeaveStatus(Connection con, LeaveApplication leaveApplication) {
        String updateQuery = "UPDATE leave_application SET status = ? WHERE user_id = ? AND date = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, leaveApplication.getStatus());
            pstmt.setInt(2, leaveApplication.getUserId());
            pstmt.setDate(3, leaveApplication.getDate()); // Set the date parameter

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave status updated successfully!");
            } else {
                System.out.println("No leave application found for the given user ID and date.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update leave status: " + e.getMessage());
        }
    }
    
    public static void resetLeaveApplications(Connection con) {
        String resetQuery = "DELETE FROM leave_application"; // Deletes all records

        try (PreparedStatement pstmt = con.prepareStatement(resetQuery)) {
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave applications reset successfully!");
            } else {
                System.out.println("No leave applications to reset.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to reset leave applications: " + e.getMessage());
        }
    }
}
