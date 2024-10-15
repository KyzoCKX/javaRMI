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

    private int leaveApplicationId;
    private int userId;
    private Date date; // Using java.sql.Date for database compatibility
    private int numberOfDays;
    private String type; // e.g., "annual", "sick", etc.
    private String status; // e.g., "pending", "approved", "rejected"
    private String reason;

    // Constructor for creating a new leave application
    public LeaveApplication(int leaveApplicationId, int userId, Date date, int numberOfDays, String type, String status, String reason) {
        this.leaveApplicationId = leaveApplicationId;
        this.userId = userId;
        this.date = date;
        this.numberOfDays = numberOfDays;
        this.type = type;
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public int getLeaveApplicationId(){return leaveApplicationId;};

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setReason(String reason){this.reason = reason;}

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getType() {
        return type;
    }
    public String getReason(){return reason;}

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
        String insertQuery = "INSERT INTO leave_application (user_id, date, number_of_days, type, status, reason) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, leaveApp.getUserId());
            pstmt.setDate(2, leaveApp.getDate());
            pstmt.setInt(3, leaveApp.getNumberOfDays());
            pstmt.setString(4, leaveApp.getType());
            pstmt.setString(5, leaveApp.getStatus());
            pstmt.setString(6, leaveApp.getReason());

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
                        rs.getInt("leave_application_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getInt("number_of_days"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("reason")
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
        String updateQuery = "UPDATE leave_application SET status = ? WHERE leave_application_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setString(1, leaveApplication.getStatus());
            pstmt.setInt(2, leaveApplication.getLeaveApplicationId());

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


    public static List<LeaveApplication> retrieveLeaveApplication(Connection con) {
        String selectQuery = "SELECT * FROM leave_application";
        List<LeaveApplication> leaveApplications = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LeaveApplication leaveApplication = new LeaveApplication(
                        rs.getInt("leave_application_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getInt("number_of_days"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("reason")
                );
                leaveApplications.add(leaveApplication);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve leave applications: " + e.getMessage());
        }
        return leaveApplications;
    }

    public static LeaveApplication getLeaveApplicationByLeaveApplicationId(Connection con, int leaveApplicationId) {
        String selectQuery = "SELECT * FROM leave_application Where leave_application_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, leaveApplicationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return new LeaveApplication(
                        rs.getInt("leave_application_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getInt("number_of_days"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("reason")
                );
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve leave applications: " + e.getMessage());
        }
        return null;
    }

    public static boolean deleteLeaveApplication(Connection con, int leaveApplicationId) {
        String deleteQuery = "DELETE FROM leave_application WHERE leave_application_id = ?"; // Deletes all records

        try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, leaveApplicationId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave applications reset successfully!");
                return true;
            } else {
                System.out.println("No leave applications to reset.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Failed to reset leave applications: " + e.getMessage());
        }
        return false;
    }

    public static List<LeaveApplication> retrieveLeaveApplicationByUserIdAndStartDateAndEndDateAndLeaveType(Connection con, int userId, Date startDate, Date endDate, String type) {
        String selectQuery = "SELECT * FROM leave_application WHERE user_id = ? AND type = ? AND date BETWEEN ? AND ?";
        List<LeaveApplication> leaveApplications = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            pstmt.setDate(3, startDate);
            pstmt.setDate(4, endDate);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LeaveApplication leaveApplication = new LeaveApplication(
                        rs.getInt("leave_application_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getInt("number_of_days"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("reason")
                );
                leaveApplications.add(leaveApplication);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve leave applications: " + e.getMessage());
            // Optionally, you can also log this error or rethrow it if necessary
        }

        return leaveApplications;
    }
}
