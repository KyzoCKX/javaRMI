package com.dcom.auth;

import com.dcom.Main;
import com.dcom.rmi.LoginService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Validator;
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
            if (!Validator.isValidEmail(email)) {
                System.out.println("Invalid email. Please enter a valid email to continue:");
                continue;
            }
            System.out.println("Enter password to continue:");
            boolean passwordIsValid = false;
            String password = null;
            while (!passwordIsValid) {
                password = Main.scanner.nextLine();
                if (!Validator.isValidPassword(password)) {
                    System.out.println("Invalid password. Please enter a valid password to continue:");
                    continue;
                }
                passwordIsValid = true;
            }

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
