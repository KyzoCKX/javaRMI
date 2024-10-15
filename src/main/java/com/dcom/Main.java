package com.dcom;

import com.dcom.auth.Login;
import com.dcom.utils.Token;
import com.dcom.utils.Navigator;
import com.dcom.utils.AsciiArt;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            AsciiArt.printDHEL();
            Token token = new Token();
            File tokenFile = new File("tokenFile.dat");
            if (tokenFile.exists()) {
                Token.loadFromFile("tokenFile.dat"); 
            }
    
            if (!Token.isTokenPresent()) {
                Login login = new Login();
                login.performLogin(); 
            } else {
                // System.out.println("Token is present: " + token);
                System.out.println("Welcome back to DHEL payroll system!");
                if ("HR".equals(Token.getDecodedToken().getUserType())) {
                    System.out.println("Navigating to HR Portal....................");
                    Navigator.navigateToHRPortal();
                }
            }
        }
        
    }
}
