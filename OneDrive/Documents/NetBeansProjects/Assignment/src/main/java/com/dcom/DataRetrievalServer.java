/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dcom;
import com.dcom.dataModel.User;
import com.dcom.dataModel.Payroll;
import com.dcom.dataModel.Employee;
import com.dcom.dataModel.SalaryClass;
import com.dcom.dataModel.LeaveApplication;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class DataRetrievalServer extends UnicastRemoteObject implements DataRetrievalInterface {
    private Connection con;

    public DataRetrievalServer(Connection con) throws RemoteException {
        super();
        this.con = con; // Initialize the database connection
    }

    // User Methods
    @Override
    public int createUser(User user, String name, double salary, int totalDaysOfWork, int availablePaidLeave) throws RemoteException {
        try {
            int userId = User.createUser(con, user, name, salary, totalDaysOfWork, availablePaidLeave);
            System.out.println("User created with ID: " + userId);
            return userId;
        } catch (Exception e) {
            System.out.println("Failed to create user: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public int getUserCountByEmail(String email) throws RemoteException{
        return User.getUserCountByEmail(con, email);
    }

    @Override
    public User getUserByEmail(String email) throws RemoteException{
        return User.getUserByEmail(con, email);
    }

    @Override
    public List<User> retrieveAllUser() throws RemoteException {
        return User.getAllUser(con);
    }

    @Override
    public User retrieveUser(int userId) throws RemoteException {
        return User.getUser(con, userId);
    }

    @Override
    public boolean updateUser(User user) throws RemoteException {
        try {
            User.updateUser(con, user);
            System.out.println("User updated: " + user.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws RemoteException {
        try {
            User.deleteUser(con, userId);
            System.out.println("User deleted with ID: " + userId);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean getUserByUserIdAndPassword(int userId, String pwd, String userType) {
        return User.getUserByUserIdAndPassword(con, userId, pwd, userType);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String pwd, String userType) throws RemoteException {
        return User.getUserByEmailAndPassword(con, email, pwd, userType);
    }

    // Employee Methods
    @Override
    public Employee retrieveEmployee(int employeeId) throws RemoteException {
        return Employee.getEmployeeById(con, employeeId);
    }

    @Override
    public List<Employee> retrieveAllEmployee() throws RemoteException {
        return Employee.getAllEmployees(con);
    }

    @Override
    public boolean updateEmployee(Employee employee) throws RemoteException {
        try {
            Employee.updateEmployee(con, employee);
            System.out.println("Employee updated: " + employee.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws RemoteException {
        try {
            Employee.deleteEmployee(con, employeeId);
            System.out.println("Employee deleted with ID: " + employeeId);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete employee: " + e.getMessage());
            return false;
        }
    }

    // Payroll Methods
    @Override
    public boolean createPayroll(Payroll payroll) throws RemoteException {
        try {
            Payroll.createPayroll(con, payroll);
            System.out.println("Payroll created with ID: " + payroll.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to create payroll: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Payroll retrievePayrollByPayRollId(int payrollId) throws RemoteException {
        return Payroll.getPayrollByPayrollId(con, payrollId);
    }

    @Override
    public List<Payroll> retrievePayrollByUserId(int userId) throws RemoteException {
        return Payroll.retrievePayrollByUserId(con, userId);
    }

    @Override
    public boolean updatePayroll(Payroll payroll) throws RemoteException {
        try {
            Payroll.updatePayroll(con, payroll);
            System.out.println("Payroll updated: " + payroll.getPayrollId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update payroll: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePayroll(int payrollId) throws RemoteException {
        try {
            Payroll.deletePayroll(con, payrollId);
            System.out.println("Payroll deleted with ID: " + payrollId);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete payroll: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Payroll> retrievePayrollList() throws RemoteException{
        return Payroll.getPayrollList(con);
    }

    // Leave Application Methods
    @Override
    public boolean createLeaveApplication(LeaveApplication leaveApplication) throws RemoteException {
        try {
            LeaveApplication.applyLeave(con, leaveApplication);
            System.out.println("Leave Application created with ID: " + leaveApplication.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to create leave application: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<LeaveApplication> retrieveLeaveApplication() throws RemoteException{
        return LeaveApplication.retrieveLeaveApplication(con);
    }

    @Override
    public LeaveApplication getLeaveApplicationByLeaveApplicationId(int leaveApplication_id) throws RemoteException{
        return LeaveApplication.getLeaveApplicationByLeaveApplicationId(con, leaveApplication_id);
    }

    @Override
    public boolean deleteLeaveApplication(int leaveApplication_id) throws RemoteException{
        return LeaveApplication.deleteLeaveApplication(con, leaveApplication_id);
    }

    @Override
    public List<LeaveApplication> retrieveLeaveApplicationByUserIdAndStartDateAndEndDateAndLeaveType(int user_id, Date startDate, Date endDate, String type) throws RemoteException{
        return LeaveApplication.retrieveLeaveApplicationByUserIdAndStartDateAndEndDateAndLeaveType(con, user_id, startDate, endDate, type);
    }

    @Override
    public List<LeaveApplication> retrieveLeaveApplicationByUserId(int userId) throws RemoteException {
        return LeaveApplication.getLeaveByUserId(con, userId);
    }

    @Override
    public boolean updateLeaveApplication(LeaveApplication leaveApplication) throws RemoteException {
        try {
            LeaveApplication.updateLeaveStatus(con, leaveApplication);
            System.out.println("Leave Application updated: " + leaveApplication.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update leave application: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean resetLeaveApplications() throws RemoteException {
        try {
            LeaveApplication.resetLeaveApplications(con); // 'con' is your database connection
            System.out.println("Leave applications reset successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Failed to reset leave applications: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public SalaryClass getSalaryClassBySalary(double salary) throws RemoteException {
        try {
            return SalaryClass.getSalaryClassBySalary(con, salary); // Assuming SalaryClass has this method
        } catch (Exception e) {
            System.out.println("Failed to retrieve salary class: " + e.getMessage());
            return null; // Return null if any failure occurs
        }
    }

    // Method to update SalaryClass
    @Override
    public boolean updateSalaryClass(SalaryClass salaryClass) throws RemoteException {
        try {
            SalaryClass.updateSalaryClass(con, salaryClass); // Assuming SalaryClass has this method
            System.out.println("Salary Class updated: " + salaryClass.getSalaryClass());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update salary class: " + e.getMessage());
            return false; // Return false if any failure occurs
        }
    }
}

