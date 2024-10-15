package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.dataModel.Employee;
import com.dcom.utils.Token;
import java.rmi.RemoteException;

public class UpdateEmployeeSalary {
    
    private EmployeeManagementService employeeService;

    public UpdateEmployeeSalary() {
        try {
            employeeService = ServiceLocator.getEmployeeManagementService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(){


        if(employeeService == null) {
            System.out.println("Connection to server failed................");
            return;
        }

        int userId = -1;
        int newSalary = -1; 
        
        while (true) {
            System.out.println("Enter the User ID of the employee you want to update: ");
            if (Main.scanner.hasNextInt()) {
                userId = Main.scanner.nextInt();  
                Main.scanner.nextLine(); 
                break; 
            } else {
                System.out.println("Invalid input. Please enter a valid User ID (integer).");
                Main.scanner.nextLine();
            }
        }

        while (true) {
            System.out.println("Enter the new salary: ");
            if (Main.scanner.hasNextInt()) {
                newSalary = Main.scanner.nextInt();  
                Main.scanner.nextLine(); 
                break; 
            } else {
                System.out.println("Invalid input. Please enter a valid salary (integer).");
                Main.scanner.nextLine(); 
            }
        }

        try {
            String token = Token.getDecodedToken().getTokenString();
            boolean updated = employeeService.updateEmployeeSalary(token, userId, newSalary);
            if (updated) {
                System.out.println("Employee salary updated successfully!");
            } else {
                System.out.println("User not found.");
                // System.out.println("Token Expired.");
                // Token.clearToken();
                // Token.deleteTokenFile("tokenFile.dat");
                // return;
            }
        } catch (RemoteException e) {
            System.err.println("Error occurred while updating employee salary. Please try again later.");
            e.printStackTrace();
        }
    }


}
