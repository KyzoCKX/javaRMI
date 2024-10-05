/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.database;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;


public class RmiServiceImplement extends UnicastRemoteObject implements RmiService{
    private Connection con;

    public RmiServiceImplement(Connection con) throws RemoteException {
        super();
        this.con = con; // Initialize the database connection
    }

    // User Methods
    @Override
    public void createUser(User user) throws RemoteException {
        int userId = User.createUser(con, user);
        System.out.println("User created with ID: " + userId);
    }
    
    @Override
    public List<User> retrieveAllUser() throws RemoteException{
        return User.getAllUser(con);
    }


    @Override
    public User retrieveUser(int userId) throws RemoteException {
        return User.getUser(con, userId);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        User.updateUser(con, user);
        System.out.println("User updated: " + user.getUserId());
    }

    @Override
    public void deleteUser(int userId) throws RemoteException {
        User.deleteUser(con, userId);
        System.out.println("User deleted with ID: " + userId);
    }

    // Employee Methods
//    @Override
//    public void createEmployee(Employee employee) throws RemoteException {
//        int employeeId = Employee.createEmployee(con, employee);
//        System.out.println("Employee created with ID: " + employeeId);
//    }

    @Override
    public Employee retrieveEmployee(int employeeId) throws RemoteException {
        return Employee.getEmployeeById(con, employeeId);
    }
    @Override
    public List<Employee> retrieveAllEmployee() throws RemoteException{
        return Employee.getAllEmployees(con);
    }


    @Override
    public void updateEmployee(Employee employee) throws RemoteException {
        Employee.updateEmployee(con, employee);
        System.out.println("Employee updated: " + employee.getUserId());
    }

    @Override
    public void deleteEmployee(int employeeId) throws RemoteException {
        Employee.deleteEmployee(con, employeeId);
        System.out.println("Employee deleted with ID: " + employeeId);
    }

    // Payroll Methods
    @Override
    public void createPayroll(Payroll payroll) throws RemoteException {
        Payroll.createPayroll(con, payroll);
        System.out.println("Payroll created with ID: " + payroll.getUserId());
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
    public void updatePayroll(Payroll payroll) throws RemoteException {
        Payroll.updatePayroll(con, payroll);
        System.out.println("Payroll updated: " + payroll.getPayrollId());
    }

    @Override
    public void deletePayroll(int payrollId) throws RemoteException {
        Payroll.deletePayroll(con, payrollId);
        System.out.println("Payroll deleted with ID: " + payrollId);
    }

    // Leave Application Methods
    @Override
    public void createLeaveApplication(LeaveApplication leaveApplication) throws RemoteException {
        LeaveApplication.applyLeave(con, leaveApplication);
        System.out.println("Leave Application created with ID: " + leaveApplication.getUserId());
    }

    @Override
    public List<LeaveApplication> retrieveLeaveApplication(int userId) throws RemoteException {
        return LeaveApplication.getLeaveByUserId(con, userId);
    }

    @Override
    public void updateLeaveApplication(LeaveApplication leaveApplication) throws RemoteException {
        LeaveApplication.updateLeaveStatus(con, leaveApplication);
        System.out.println("Leave Application updated: " + leaveApplication.getUserId());
    }
    
    @Override
    public void resetLeaveApplications() throws RemoteException {
        LeaveApplication.resetLeaveApplications(con); // 'con' is your database connection
    }

//    @Override
//    public void deleteLeaveApplication(int applicationId) throws RemoteException {
//        LeaveApplication.deleteLeaveApplication(con, applicationId);
//        System.out.println("Leave Application deleted with ID: " + applicationId);
//    }
}
