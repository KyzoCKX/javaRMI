package com.dcom.hr;

import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.Scanner;
import com.dcom.utils.AsciiArt;
import com.dcom.Main;
import com.dcom.dataModel.Employee;

public class SearchEmployeePage {

    private EmployeeManagementService employeeService;

    public SearchEmployeePage() {
        try {
            employeeService = ServiceLocator.getEmployeeManagementService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {

        if(employeeService == null) {
            System.out.println("Connection to server failed................");
            return;
        }

        int userId = -1; 
        
        while (true) {
            System.out.print("Enter User Id: ");
            if (Main.scanner.hasNextInt()) {
                userId = Main.scanner.nextInt();  
                Main.scanner.nextLine();   
                break; 
            } else {
                System.out.println("Invalid input. Please enter a valid User ID (integer).");
                Main.scanner.nextLine(); 
            }
        }
        
        String token = Token.getDecodedToken().getTokenString();  
        try {
            Employee employee = employeeService.retrieveEmployeeInfo(token, userId);
            if (employee != null) {
                System.out.println("┌────────────────────────────┬───────────────────────────────────┐");
                System.out.printf("│ %-26s │ %-33s │%n", "Field", "Value");
                System.out.println("├────────────────────────────┼───────────────────────────────────┤");
                System.out.printf("│ %-26s │ %-33d │%n", "User ID", employee.getUserId());
                System.out.printf("│ %-26s │ %-33s │%n", "Name", employee.getName());
                System.out.printf("│ %-26s │ %-33.2f │%n", "Salary", employee.getSalary());
                System.out.printf("│ %-26s │ %-33d │%n", "Total Days Worked", employee.getTotalDaysOfWork());
                System.out.printf("│ %-26s │ %-33d │%n", "Available Paid Leave", employee.getAvailablePaidLeave());
                System.out.println("└────────────────────────────┴───────────────────────────────────┘");
            } else {
                System.out.println("Employee with User ID " + userId + " not found.");
                // issue: when token is expired, the user is not notified.
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
