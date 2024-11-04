package com.dcom.employee;

import com.dcom.rmi.LeaveApplicationService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.AbstractMap;
import java.util.Scanner;

public class ApplyForLeavePage {

    private LeaveApplicationService leaveService;

    public ApplyForLeavePage() {
        try {
            leaveService = ServiceLocator.getLeaveApplicationService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if (leaveService == null) {
            System.out.println("Connection to server failed.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Apply for Leave ---");

        System.out.print("Enter start date of leave (yyyy-mm-dd): ");
        String dateInput = scanner.nextLine();
        Date startDate = Date.valueOf(dateInput);  // Converts string to SQL Date

        System.out.print("Enter number of days: ");
        int numberOfDays = scanner.nextInt();
        scanner.nextLine();  // Consume newline character

        System.out.print("Enter type of leave (e.g., annual, sick): ");
        String leaveType = scanner.nextLine();

        System.out.print("Enter reason for leave: ");
        String reason = scanner.nextLine();

        // Get the token from the current session
        String token = Token.getToken();

        try {
            AbstractMap.Entry<Boolean, String> result = leaveService.applyLeave(token, startDate, numberOfDays, leaveType, reason);

            if (result.getKey()) {
                System.out.println("Leave application submitted successfully.");
            } else if ("invalid request".equals(result.getValue())) {
                System.out.println("Session expired. Please log in again.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
            } else {
                System.out.println("Failed to submit leave application: " + result.getValue());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
