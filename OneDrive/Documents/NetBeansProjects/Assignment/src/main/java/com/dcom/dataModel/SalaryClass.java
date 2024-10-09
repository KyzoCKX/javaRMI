
package com.dcom.dataModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class SalaryClass  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String salaryClass;
    private double threshold;
    private double taxPercentage;
    private double epfPercentage;
    
    public SalaryClass(String salaryClass, double threshold, double taxPercentage, double epfPercentage) {
        this.salaryClass = salaryClass;
        this.threshold = threshold;
        this.taxPercentage = taxPercentage;
        this.epfPercentage = epfPercentage;
    }

    // Getters
    public String getSalaryClass() {
        return salaryClass;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public double getEpfPercentage() {
        return epfPercentage;
    }

    // Setters
    public void setSalaryClass(String salaryClass) {
        this.salaryClass = salaryClass;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public void setEpfPercentage(double epfPercentage) {
        this.epfPercentage = epfPercentage;
    }

    // Static method to retrieve a SalaryClass by passing a salary
    public static SalaryClass getSalaryClassBySalary(Connection con, double salary) {
        String selectQuery = "SELECT * FROM salary_class WHERE threshold >= ? ORDER BY threshold ASC LIMIT 1";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setDouble(1, salary);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new SalaryClass(
                    rs.getString("class"),
                    rs.getDouble("threshold"),
                    rs.getDouble("tax_percentage"),
                    rs.getDouble("epf_percentage")
                );
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve salary class: " + e.getMessage());
        }
        return null; // Return null if no matching class is found
    }
    public static boolean updateSalaryClass(Connection con, SalaryClass salaryClass) {
        String updateQuery = "UPDATE salary_class SET threshold = ?, tax_percentage = ?, epf_percentage = ? WHERE class = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, salaryClass.getThreshold());
            pstmt.setDouble(2, salaryClass.getTaxPercentage());
            pstmt.setDouble(3, salaryClass.getEpfPercentage());
            pstmt.setString(4, salaryClass.getSalaryClass());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Salary class " + salaryClass + " updated successfully.");
                return true;
            } else {
                System.out.println("No rows updated for salary class " + salaryClass);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update salary class: " + e.getMessage());
        }
        return false;
    }
}
