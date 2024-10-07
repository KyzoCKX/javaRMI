package com.dcom.dataModel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private int payrollId;
    private double totalPaid;
    private boolean paid; // True if paid, false if not
    private double tax;
    private String salaryClass; // Example: "A", "B", etc.
    private double percentage; // Tax or other percentage
    private Date date;

    // Constructor for creating a new payroll entry
    public Payroll(int userId, double totalPaid, boolean paid, double tax, String salaryClass, double percentage, Date date) {
        this.userId = userId;
        this.totalPaid = totalPaid;
        this.paid = paid;
        this.tax = tax;
        this.salaryClass = salaryClass;
        this.percentage = percentage;
        this.date = date;
    }

    // Constructor for retrieving an existing payroll entry
    public Payroll(int userId, int payrollId, double totalPaid, boolean paid, double tax, String salaryClass, double percentage, Date date) {
        this.userId = userId;
        this.payrollId = payrollId;
        this.totalPaid = totalPaid;
        this.paid = paid;
        this.tax = tax;
        this.salaryClass = salaryClass;
        this.percentage = percentage;
        this.date = date;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public int getPayrollId() {
        return payrollId;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getSalaryClass() {
        return salaryClass;
    }

    public void setSalaryClass(String salaryClass) {
        this.salaryClass = salaryClass;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Method to insert a payroll record into the database
    public static void createPayroll(Connection con, Payroll payroll) {
        String insertQuery = "INSERT INTO payroll (user_id, total_paid, paid, tax, salary_class, percentage, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, payroll.getUserId());
            pstmt.setDouble(2, payroll.getTotalPaid());
            pstmt.setBoolean(3, payroll.isPaid());
            pstmt.setDouble(4, payroll.getTax());
            pstmt.setString(5, payroll.getSalaryClass());
            pstmt.setDouble(6, payroll.getPercentage());
            pstmt.setDate(7, payroll.getDate());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Payroll record created successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert payroll record: " + e.getMessage());
        }
    }

    // Method to get payroll details by payroll ID
    public static Payroll getPayrollByPayrollId(Connection con, int payrollId) {
        String selectQuery = "SELECT * FROM payroll WHERE payroll_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, payrollId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Payroll(
                        rs.getInt("user_id"),
                        rs.getInt("payroll_id"),
                        rs.getDouble("total_paid"),
                        rs.getBoolean("paid"),
                        rs.getDouble("tax"),
                        rs.getString("salary_class"),
                        rs.getDouble("percentage"),
                        rs.getDate("date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }
        return null; // No record found
    }
    
    public static List<Payroll> retrievePayrollByUserId(Connection con,int userId) throws RemoteException {
        List<Payroll> payrollList = new ArrayList<>();
        String selectQuery = "SELECT * FROM payroll WHERE user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Payroll payroll = new Payroll(
                        rs.getInt("user_id"),
                        rs.getInt("payroll_id"),
                        rs.getDouble("total_paid"),
                        rs.getBoolean("paid"),
                        rs.getDouble("tax"),
                        rs.getString("salary_class"),
                        rs.getDouble("percentage"),
                        rs.getDate("date")
                );
                payrollList.add(payroll);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }

        return payrollList; // Return the list of Payroll records
    }

    // Method to update payroll details
    public static void updatePayroll(Connection con, Payroll payroll) {
        String updateQuery = "UPDATE payroll SET total_paid = ?, paid = ?, tax = ?, salary_class = ?, percentage = ?, date = ? WHERE payroll_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, payroll.getTotalPaid());
            pstmt.setBoolean(2, payroll.isPaid());
            pstmt.setDouble(3, payroll.getTax());
            pstmt.setString(4, payroll.getSalaryClass());
            pstmt.setDouble(5, payroll.getPercentage());
            pstmt.setDate(6, payroll.getDate());
            pstmt.setInt(7, payroll.getPayrollId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Payroll record updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update payroll record: " + e.getMessage());
        }
    }

    // Method to delete a payroll record
    public static void deletePayroll(Connection con, int payrollId) {
        String deleteQuery = "DELETE FROM payroll WHERE payroll_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, payrollId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Payroll record deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete payroll record: " + e.getMessage());
        }
    }
}
