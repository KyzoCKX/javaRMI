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
import java.sql.Date;
import java.util.List;

public interface DataRetrievalInterface extends Remote {
    //user class
    int createUser(User user, String name, double salary, int totalDaysOfWork, int availablePaidLeave) throws RemoteException;

    int getUserCountByEmail(String email) throws RemoteException;
    User getUserByEmail(String email) throws RemoteException;

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
    List<Payroll> retrievePayrollList() throws RemoteException;

    // leave application class
    boolean createLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;

    List<LeaveApplication> retrieveLeaveApplication() throws RemoteException;
    LeaveApplication getLeaveApplicationByLeaveApplicationId(int leaveApplication_id) throws RemoteException;
    boolean deleteLeaveApplication(int leaveApplication_id) throws RemoteException;
    List<LeaveApplication> retrieveLeaveApplicationByUserIdAndStartDateAndEndDateAndLeaveType(int user_id, Date startDate, Date endDate, String type) throws RemoteException;

    List<LeaveApplication> retrieveLeaveApplicationByUserId(int userId) throws RemoteException;
    boolean updateLeaveApplication(LeaveApplication leaveApplication) throws RemoteException;
    boolean resetLeaveApplications() throws RemoteException;
    
    // salary class
    SalaryClass getSalaryClassBySalary(double salary) throws RemoteException;
    boolean updateSalaryClass(SalaryClass salaryClass) throws RemoteException;
}

