package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.Scanner;
import com.dcom.utils.AsciiArt;

public class CreateEmployeeAccountPage {

    private EmployeeManagementService employeeService;

    public CreateEmployeeAccountPage() {
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

        System.out.println("Create New Employee Account");
        System.out.print("Enter employee name: ");
        String name = Main.scanner.nextLine();

        System.out.print("Enter employee email: ");
        String email = Main.scanner.nextLine();

        String userType = getUserType();

        System.out.print("Enter employee password: ");
        String pwd = Main.scanner.nextLine();

        System.out.print("Enter employee salary: ");
        double salary = Main.scanner.nextDouble();

        Main.scanner.nextLine(); // Consume the newline character


        String token = Token.getDecodedToken().getTokenString();  

        try {
            AbstractMap.Entry<Boolean, String> result = employeeService.createUser(token, email, userType, pwd, name, salary);
            if (result.getKey()) {
                System.out.println("Employee created successfully.");
            } else if(result.getValue().equals("invalid request")){
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return;
            } else {
                System.out.println("Failed to create employee: " + result.getValue());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        
    }

    private String getUserType() {
        String userType = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Select employee user type:");
            System.out.println("1. HR");
            System.out.println("2. Employee");
            System.out.print("Enter your choice (1 or 2): ");

            if (!Main.scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1 for HR or 2 for Employee.");
                Main.scanner.nextLine(); // Consume the invalid input
                continue;
            }

            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    userType = "HR";
                    validInput = true; 
                    break;
                case 2:
                    userType = "Employee";
                    validInput = true; 
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1 for HR or 2 for Employee.");
                    AsciiArt.printDivider();
            }
        }
        return userType; 
    }
}
