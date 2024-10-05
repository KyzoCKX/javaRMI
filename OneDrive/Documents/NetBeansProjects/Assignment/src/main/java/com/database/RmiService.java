/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.database;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiService  extends Remote{
    void createUser(User user) throws RemoteException;
    User retrieveUser(int userId) throws RemoteException;
    List<User> retrieveAllUser() throws RemoteException;
    void updateUser(User user) throws RemoteException;
    void deleteUser(int userId) throws RemoteException;

//    void createEmployee(Employee employee) throws RemoteException;
    Employee retrieveEmployee(int employeeId) throws RemoteException;
    List<Employee> retrieveAllEmployee() throws RemoteException;
    void updateEmployee(Employee employee) throws RemoteException;
    void deleteEmployee(int employeeId) throws RemoteException;

    void createPayroll(Payroll payroll) throws RemoteException;
    Payroll retrievePayrollByPayRollId(int payrollId) throws RemoteException;
    List<Payroll> retrievePayrollByUserId(int userId) throws RemoteException;
    void updatePayroll(Payroll payroll) throws RemoteException;
    void deletePayroll(int payrollId) throws RemoteException;

    void createLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;
    List<LeaveApplication> retrieveLeaveApplication(int userId) throws RemoteException;
    void updateLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;
    void resetLeaveApplications() throws  RemoteException;
}
