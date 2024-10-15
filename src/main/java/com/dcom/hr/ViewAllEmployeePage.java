package com.dcom.hr;

import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import java.rmi.RemoteException;
import com.dcom.dataModel.Employee;
import java.util.List;

public class ViewAllEmployeePage {

    private static final int NAME_MAX_LENGTH = 18; // Maximum length for name field

    private EmployeeManagementService employeeService;

    public ViewAllEmployeePage() {
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

        try {
            String token = Token.getDecodedToken().getTokenString();
            List<Employee> employees = employeeService.retrieveAllEmployee(token);

            if(employees == null) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return;
            }

            System.out.println(employees.size() + " employees found.");
            
            printTableHeader();

            for (int i = 0; i < employees.size(); i++) {
                printEmployeeRow(employees.get(i));
                if (i < employees.size() - 1) {
                    printRowDivider();
                }
            }

            printTableFooter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printTableHeader() {
        System.out.println("┌──────────┬────────────────────┬──────────┬───────────────────┬───────────────────────┐");
        System.out.printf("│ %-8s │ %-18s │ %-8s │ %-17s │ %-21s │%n", "User ID", "Name", "Salary", "Total Days Worked", "Available Paid Leave");
        System.out.println("├──────────┼────────────────────┼──────────┼───────────────────┼───────────────────────┤");
    }

    private void printEmployeeRow(Employee employee) {
        String truncatedName = truncateString(employee.getName(), NAME_MAX_LENGTH);
        System.out.printf("│ %-8s │ %-18s │ %-8.2f │ %-17d │ %-21d │%n", 
            employee.getUserId(), 
            truncatedName, 
            employee.getSalary(), 
            employee.getTotalDaysOfWork(), 
            employee.getAvailablePaidLeave());
    }

    private String truncateString(String value, int maxLength) {
        if (value.length() > maxLength) {
            return value.substring(0, maxLength - 3) + "..."; 
        }
        return value; 
    }

    private void printRowDivider() {
        System.out.println("├──────────┼────────────────────┼──────────┼───────────────────┼───────────────────────┤");
    }

    private void printTableFooter() {
        System.out.println("└──────────┴────────────────────┴──────────┴───────────────────┴───────────────────────┘");
    }
}
