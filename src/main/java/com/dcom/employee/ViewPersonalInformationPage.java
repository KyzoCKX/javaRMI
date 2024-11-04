package com.dcom.employee;

import com.dcom.rmi.EmployeeManagementService;
import com.dcom.dataModel.Employee;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;

import java.rmi.RemoteException;

public class ViewPersonalInformationPage {
    
    private EmployeeManagementService employeeService;

    public ViewPersonalInformationPage() {
        try {
            employeeService = ServiceLocator.getEmployeeManagementService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if (employeeService == null) {
            System.out.println("Connection to server failed.");
            return;
        }

        String token = Token.getToken();
        int userId = Token.getDecodedToken().getUserId();

        try {
            Employee employee = employeeService.retrieveEmployeeInfo(token, userId);
            if (employee != null) {
                displayEmployeeInfo(employee);
            } else {
                System.out.println("No employee information found.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve employee information.");
        }
    }

    private void displayEmployeeInfo(Employee employee) {
        System.out.println("\n--- Employee Personal Information ---");
        System.out.println("User ID: " + employee.getUserId());
        System.out.println("Name: " + employee.getName());
        System.out.println("Salary: $" + employee.getSalary());
        System.out.println("Total Days of Work: " + employee.getTotalDaysOfWork());
        System.out.println("Available Paid Leave: " + employee.getAvailablePaidLeave());
    }
}
