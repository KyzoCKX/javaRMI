package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.UserService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import java.rmi.RemoteException;
import java.util.Scanner;

public class UpdatePasswordPage {

    private UserService userService;

    public UpdatePasswordPage() {
        try {
            userService = ServiceLocator.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {

        if(userService == null) {
            System.out.println("Connection to server failed................");
            return;
        }

        System.out.println("Enter your current password:");
        String currentPassword = Main.scanner.nextLine();
        System.out.println("Enter your new password:");
        String newPassword = Main.scanner.nextLine();

        try {
            boolean updated = userService.updatePassword(Token.getDecodedToken().getTokenString(), currentPassword, newPassword);
            if (updated) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return;
            }
        } catch (RemoteException e) {
            System.err.println("Error occurred while updating password. Please try again later.");
            e.printStackTrace();
        }
    }
}
