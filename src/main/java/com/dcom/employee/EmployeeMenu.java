package com.dcom.employee;

import com.dcom.Main;
import com.dcom.utils.AsciiArt;
import com.dcom.utils.Token;

public class EmployeeMenu {
    
    public void showMenu() {
        int choice;

        while (true) {

            if (!Token.isTokenPresent()) {
                break;
            }

            AsciiArt.printDivider();

            System.out.println("\n--- Employee Portal Menu ---");
            System.out.println("1. View Personal Information");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Payroll History");
            System.out.println("4. Apply for Leave");
            System.out.println("5. View Leave Applications");
            System.out.println("6. Logout");
            System.out.print("======== Enter number to continue =========");

            if (!Main.scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.......");
                Main.scanner.nextLine(); // Consume the invalid input
                continue;
            }

            choice = Main.scanner.nextInt();
            Main.scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1: // View Personal Information
                    ViewPersonalInformationPage viewPersonalInfoPage = new ViewPersonalInformationPage();
                    viewPersonalInfoPage.show();
                    break;

                case 2: // Update Personal Information
                    UpdatePersonalInfoPage updatePersonalInfoPage = new UpdatePersonalInfoPage();
                    int loggedInEmployeeId = Token.getDecodedToken().getUserId(); // Retrieve employee ID from token
                    updatePersonalInfoPage.show(loggedInEmployeeId); // Pass ID to show method
                    break;

                case 3: // View Payroll History
                    ViewPayrollHistoryPage viewPayrollHistoryPage = new ViewPayrollHistoryPage();
                    viewPayrollHistoryPage.show();
                    break;

                case 4: // Apply for Leave
                    ApplyForLeavePage applyForLeavePage = new ApplyForLeavePage();
                    applyForLeavePage.show();
                    break;

                case 5: // View Leave Applications
                    ViewLeaveApplicationPage viewLeaveApplicationPage = new ViewLeaveApplicationPage();
                    viewLeaveApplicationPage.show();
                    break;

                case 6: // Logout
                    System.out.println("Logging out...");
                    Token.clearToken();
                    Token.deleteTokenFile("tokenFile.dat");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
