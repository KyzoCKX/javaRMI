package com.dcom.auth;

import com.dcom.Main;
import com.dcom.rmi.LoginService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Navigator;
import com.dcom.utils.Token;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Login {

    private LoginService loginService;

    public Login() {
        try {
            loginService = ServiceLocator.getLoginService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void performLogin() {
        boolean loggedIn = false;

        System.out.println("Welcome to DHEL payroll system! Please enter email to continue:");

        while (!loggedIn) {
            String email = Main.scanner.nextLine();
            System.out.println("Enter password to continue:");
            String password = Main.scanner.nextLine();

            try {
                String token = loginService.login(email, password);
                if(token != null) {
                    System.out.println("Login successful!");
                    if(!Token.isTokenPresent()) {
                        Token.setToken(token);
                        Token.setDecodedToken(Token.decodeToken());
                        Token.saveToFile("tokenFile.dat");
                        loggedIn = true;
                        Navigator.navigateToHRPortal();
                    }
                    else if ("HR".equals(Token.getDecodedToken().getUserType())) {
                        System.out.println("Navigating to HR Portal....................");
                        loggedIn = true;
                        Navigator.navigateToHRPortal();
                    }
                } else {
                    System.out.println("Login failed. Please enter email to try again:");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
    }
}
