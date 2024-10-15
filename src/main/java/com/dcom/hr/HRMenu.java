package com.dcom.hr;

import java.util.Scanner;

import com.dcom.Main;
import com.dcom.utils.AsciiArt;
import com.dcom.utils.Token;

public class HRMenu {

    public void showMenu() {
        int choice;

        while (true) {

            if(!Token.isTokenPresent()) {
                break;
            }

            AsciiArt.printDivider();

            System.out.println("========== HR Portal _ Main Menu ==========");
            System.out.println("1. Update Password");
            System.out.println("2. Create New Employee Account");
            System.out.println("3. View All Employees");
            System.out.println("4. Search Employee");
            System.out.println("5. Update Employee Salary");
            System.out.println("6. Manage Leave Application");
            System.out.println("7. Manage Employee Payroll");
            System.out.println("8. Generate Report");
            System.out.println("0. Logout");
            System.out.println("======== Enter number to continue =========");

            if (!Main.scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.......");
                Main.scanner.nextLine(); // Consume the invalid input
                continue; 
            }

            choice = Main.scanner.nextInt();
            Main.scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    UpdatePasswordPage updatePasswordPage = new UpdatePasswordPage(); 
                    updatePasswordPage.show();
                    break;
                case 2:
                    CreateEmployeeAccountPage createEmployeeAccountPage = new CreateEmployeeAccountPage(); 
                    createEmployeeAccountPage.show();
                    break;
                case 3:
                    ViewAllEmployeePage viewAllEmployeePage = new ViewAllEmployeePage();
                    viewAllEmployeePage.show();
                    break;
                case 4:
                    SearchEmployeePage searchEmployeePage = new SearchEmployeePage(); 
                    searchEmployeePage.show();
                    break;
                case 5:
                    UpdateEmployeeSalary updateEmployeeSalary = new UpdateEmployeeSalary();
                    updateEmployeeSalary.show();
                    break;
                case 6:
                    ManageLeaveApplicationPage manageLeaveApplicationPage = new ManageLeaveApplicationPage();
                    manageLeaveApplicationPage.show();
                    break;
                case 7:
                    ManageEmployeePayrollPage manageEmployeePayrollPage = new ManageEmployeePayrollPage();
                    manageEmployeePayrollPage.show();
                    break;
                case 8:
                    GenerateReportPage generateReportPage = new GenerateReportPage();
                    generateReportPage.show();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    Token.clearToken();
                    Token.deleteTokenFile("tokenFile.dat");
                    return; 
                default:
                    System.out.println("Invalid option. Please try again...........");
            }
        }
    }
}
