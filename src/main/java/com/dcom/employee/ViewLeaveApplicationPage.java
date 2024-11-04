package com.dcom.employee;

import com.dcom.rmi.LeaveApplicationService;
import com.dcom.dataModel.LeaveApplication;
import com.dcom.utils.Token;
import com.dcom.serviceLocator.ServiceLocator;

import java.rmi.RemoteException;
import java.util.List;

public class ViewLeaveApplicationPage {

    private LeaveApplicationService leaveApplicationService;

    public ViewLeaveApplicationPage() {
        try {
            leaveApplicationService = ServiceLocator.getLeaveApplicationService();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the LeaveApplicationService.");
        }
    }

    public void show() {
        if (leaveApplicationService == null) {
            System.out.println("Connection to server failed.");
            return;
        }

        String token = Token.getToken();  // Get the token for authentication
        if (token == null || token.isEmpty()) {
            System.out.println("User not authenticated. Please log in again.");
            return;
        }

        try {
            List<LeaveApplication> leaveApplications = leaveApplicationService.retrieveLeaveApplicationForEmployee(token);

            if (leaveApplications != null && !leaveApplications.isEmpty()) {
                System.out.println("Your Leave Applications:");
                System.out.println("--------------------------------------------------");
                for (LeaveApplication leaveApp : leaveApplications) {
                    System.out.printf("ID: %d, Date: %s, Days: %d, Type: %s, Status: %s, Reason: %s%n",
                            leaveApp.getLeaveApplicationId(),
                            leaveApp.getDate(),
                            leaveApp.getNumberOfDays(),
                            leaveApp.getType(),
                            leaveApp.getStatus(),
                            leaveApp.getReason());
                }
                System.out.println("--------------------------------------------------");
            } else {
                System.out.println("No leave applications found.");
            }
        } catch (RemoteException e) {
            System.out.println("Error retrieving leave applications: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
