package com.dcom.dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Import for handling query results
import java.sql.SQLException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable{
    private static final long serialVersionUID = 1L; // This helps during deserialization

    
    private int userId;
    private String name;
    private double salary;
    private int totalDaysOfWork;
    private int availablePaidLeave;

    // Constructor
    public Employee(int userId, String name, double salary, int totalDaysOfWork, int availablePaidLeave) {
        this.userId = userId;
        this.name = name;
        this.salary = salary;
        this.totalDaysOfWork = totalDaysOfWork;
        this.availablePaidLeave = availablePaidLeave;
    }

    // Constructor with default values
    public Employee(int userId) {
        this(userId, "unknown", 0, 20, 10); // Default values for name, salary, etc.
    }

    // Insert Employee automatically when User is created
    public static void createEmployee(Connection con, int userId, String name, double salary, int totalDaysOfWork, int availablePaidLeave) {
        String insertQuery = "INSERT INTO employee (user_id, name, salary, total_days_of_work, available_paid_leave) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, userId);
            pstmt.setString(2, name); // Default name
            pstmt.setDouble(3, salary);         // Default salary
            pstmt.setInt(4, totalDaysOfWork);           // Default total days of work
            pstmt.setInt(5, availablePaidLeave);           // Default available paid leave

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee record created successfully for userId: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to create employee record: " + e.getMessage());
        }
    }

    // Update Employee details
    public static void updateEmployee(Connection con, Employee employee) {
        String updateQuery = "UPDATE employee SET name = ?, salary = ?, available_paid_leave = ? WHERE user_id = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, employee.getName());
            pstmt.setDouble(2, employee.getSalary());
            pstmt.setInt(3, employee.getUserId());
            pstmt.setInt(4, employee.getAvailablePaidLeave());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully for userId: " + employee.getUserId());
            }
        } catch (SQLException e) {
            System.out.println("Failed to update employee: " + e.getMessage());
        }
    }

    // Retrieve Employee by userId
    public static Employee getEmployeeById(Connection con, int userId) {
        String selectQuery = "SELECT * FROM employee WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery(); // Import ResultSet to handle the result
            if (rs.next()) {
                return new Employee(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getDouble("salary"),
                        rs.getInt("total_days_of_work"),
                        rs.getInt("available_paid_leave")
                );
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve employee: " + e.getMessage());
        }
        return null;
    }
    public static List<Employee> getAllEmployees(Connection con){
        List<Employee> employees = new ArrayList<>();
        String selectQuery = "SELECT * FROM public.employee";
        try (PreparedStatement pstmt = con.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getDouble("salary"),
                        rs.getInt("total_days_of_work"),
                        rs.getInt("available_paid_leave"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: " + e.getMessage());
        }

        return employees; // Return the list of Payroll records

    }
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public int getTotalDaysOfWork() {
        return totalDaysOfWork;
    }

    public int getAvailablePaidLeave() {
        return availablePaidLeave;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalDaysOfWork(int totalDaysOfWork) {
        this.totalDaysOfWork = totalDaysOfWork;
    }

    public void setAvailablePaidLeave(int availablePaidLeave) {
        this.availablePaidLeave = availablePaidLeave;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    

    // Disable delete function by commenting it out
    
    public static void deleteEmployee(Connection con, int userId) {
        String deleteQuery = "DELETE FROM employee WHERE user_id = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(deleteQuery);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully for userId: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete employee: " + e.getMessage());
        }
    }
    
}
