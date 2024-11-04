package com.dcom.employee;

import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import com.dcom.dataModel.Employee;
import com.dcom.rmi.UserService;
import java.rmi.RemoteException;
import java.util.Scanner;

        
public class UpdatePersonalInfoPage {

    private UserService userService;
    private Scanner scanner = new Scanner(System.in);
    public UpdatePersonalInfoPage() {
        try {
            // Retrieve the user service from the ServiceLocator
            userService = ServiceLocator.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(int loggedInEmployeeId) {
        if (userService == null) {
            System.out.println("Connection to server failed.");
            return;
        }

        String token = Token.getToken(); // Get the current session token

        try {
            // Prompt for current password and new password
            System.out.println("\n--- Change Password ---");
            System.out.print("Enter your current password: ");
            String currentPassword = scanner.nextLine().trim();

            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine().trim();

            System.out.print("Confirm your new password: ");
            String confirmPassword = scanner.nextLine().trim();

            // Check if new passwords match
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("New passwords do not match. Please try again.");
                return;
            }

            // Attempt to update the password on the server
            boolean passwordUpdated = userService.updatePassword(token, currentPassword, newPassword);

            if (passwordUpdated) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password. Please check your current password and try again.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error while updating password. Please try again later.");
        }
    }
}
 