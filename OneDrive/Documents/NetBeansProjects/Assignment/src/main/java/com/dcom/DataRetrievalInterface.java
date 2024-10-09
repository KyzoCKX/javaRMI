/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dcom;
import com.dcom.dataModel.User;
import com.dcom.dataModel.Payroll;
import com.dcom.dataModel.Employee;
import com.dcom.dataModel.LeaveApplication;
import com.dcom.dataModel.SalaryClass;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DataRetrievalInterface extends Remote {
    //user class
    int createUser(User user) throws RemoteException;
    User retrieveUser(int userId) throws RemoteException;
    List<User> retrieveAllUser() throws RemoteException;
    boolean updateUser(User user) throws RemoteException;
    boolean deleteUser(int userId) throws RemoteException;
    boolean getUserByUserIdAndPassword(int userId, String pwd, String userType) throws RemoteException;
    User getUserByEmailAndPassword(String email, String pwd, String userType) throws RemoteException;
    
    // employee class
    Employee retrieveEmployee(int employeeId) throws RemoteException;
    List<Employee> retrieveAllEmployee() throws RemoteException;
    boolean updateEmployee(Employee employee) throws RemoteException;
    boolean deleteEmployee(int employeeId) throws RemoteException;

    //pay roll class
    boolean createPayroll(Payroll payroll) throws RemoteException;
    Payroll retrievePayrollByPayRollId(int payrollId) throws RemoteException;
    List<Payroll> retrievePayrollByUserId(int userId) throws RemoteException;
    boolean updatePayroll(Payroll payroll) throws RemoteException;
    boolean deletePayroll(int payrollId) throws RemoteException;

    // leave application class
    boolean createLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;
    List<LeaveApplication> retrieveLeaveApplication(int userId) throws RemoteException;
    boolean updateLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;
    boolean resetLeaveApplications() throws RemoteException;
    
    // salary class
    SalaryClass getSalaryClassBySalary(double salary) throws RemoteException;
    boolean updateSalaryClass(SalaryClass salaryClass) throws RemoteException;
}

