package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.LeaveApplicationService;
import com.dcom.rmi.EmployeeManagementService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;
import java.rmi.RemoteException;
import com.dcom.dataModel.LeaveApplication;
import com.dcom.dataModel.Employee;

import java.util.AbstractMap;
import java.util.List;

public class ManageLeaveApplicationPage {
    
    private LeaveApplicationService leaveApplicationService;
    private EmployeeManagementService employeeService;

    public ManageLeaveApplicationPage() {
        try {
            leaveApplicationService = ServiceLocator.getLeaveApplicationService();
            employeeService = ServiceLocator.getEmployeeManagementService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(){

        if(employeeService == null || leaveApplicationService == null) {
            System.out.println("Connection to server failed................");
            return;
        }

        int choice = -1;

        while (true) {
            System.out.println("======== Manage Leave Application =========");
            System.out.println("1. View All");
            System.out.println("2. Approve");
            System.out.println("3. Reject");
            System.out.println("0. Go back");
            System.out.println("============= Enter your choice ===========");


            if (!Main.scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.......");
                Main.scanner.nextLine();
                continue; 
            }

            choice = Main.scanner.nextInt();
            Main.scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    viewAll();
                    break;
                case 2:
                    boolean approved = approve();
                    if (!approved) {
                        return;
                    }
                    break;
                case 3:
                    boolean rejected = reject();
                    if (!rejected) {
                        return;
                    }
                    break;
                case 0:
                    System.out.println("Going back...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again...........");
            }
        }
    }

    private void viewAll() {
        String token = Token.getDecodedToken().getTokenString();
        try {
            List<LeaveApplication> leaveApplications = leaveApplicationService.retrieveLeaveApplicationForAllEmployee(token);
            // System.out.println("Leave Applications: " + leaveApplications);
            if (leaveApplications != null && !leaveApplications.isEmpty()) {

                leaveApplications.sort((a, b) -> Integer.compare(a.getUserId(), b.getUserId()));

                System.out.println("┌──────────┬──────────┬───────────────┬──────────────────────────────┬────────────┬──────────┬──────────┐");
                System.out.printf("│ %-8s │ %-8s │ %-13s │ %-28s │ %-10s │ %-8s │ %-8s │%n",
                        "Leave ID", "User ID", "Leave Type", "Reason", "Start Date", "Days", "Status");
                System.out.println("├──────────┼──────────┼───────────────┼──────────────────────────────┼────────────┼──────────┼──────────┤");
    
                int totalApplications = leaveApplications.size();
                for (int i = 0; i < totalApplications; i++) {
                    LeaveApplication leaveApplication = leaveApplications.get(i);
                    System.out.printf("│ %-8d │ %-8d │ %-13s │ %-28s │ %-10s │ %-8d │ %-8s │%n",
                            leaveApplication.getLeaveApplicationId(),
                            leaveApplication.getUserId(),
                            leaveApplication.getType(),
                            truncate(leaveApplication.getReason(), 50),  
                            leaveApplication.getDate(),
                            leaveApplication.getNumberOfDays(),
                            leaveApplication.getStatus());
    
                    if (i < totalApplications - 1) {
                        System.out.println("├──────────┼──────────┼───────────────┼──────────────────────────────┼────────────┼──────────┼──────────┤");
                    }
                }
                System.out.println("└──────────┴──────────┴───────────────┴──────────────────────────────┴────────────┴──────────┴──────────┘");
            } else {
                System.out.println("No leave applications found.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean approve(){
        String token = Token.getDecodedToken().getTokenString();
        try{
            int leaveId = -1;

            while (true) {
                System.out.println("Enter the Leave ID of the leave application you want to approve: ");
                if (Main.scanner.hasNextInt()) {
                    leaveId = Main.scanner.nextInt();  
                    Main.scanner.nextLine();   
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid Leave ID (integer).");
                    Main.scanner.nextLine(); 
                }
            }

            AbstractMap.Entry<Boolean, String> result = leaveApplicationService.processLeaveApplication(token, leaveId, "Approved");

            if (result.getKey()) {
                System.out.println("Leave application approved successfully!");
                
                List<LeaveApplication> leaveApplications = leaveApplicationService.retrieveLeaveApplicationForAllEmployee(token);
                LeaveApplication approvedLeaveApplication = null;

                for (LeaveApplication leaveApplication : leaveApplications) {
                    if (leaveApplication.getLeaveApplicationId() == leaveId) {
                        approvedLeaveApplication = leaveApplication;
                        break;
                    }
                }

                if (approvedLeaveApplication != null && approvedLeaveApplication.getType().toLowerCase().equals("paid")) {
                    int userId = approvedLeaveApplication.getUserId();
                    Employee employeeInfo = employeeService.retrieveEmployeeInfo(token, userId);
                    if (employeeInfo != null) {
                        int availablePaidLeave = employeeInfo.getAvailablePaidLeave();
                        int numberOfDays = approvedLeaveApplication.getNumberOfDays();
                        int newAvailablePaidLeave = availablePaidLeave - numberOfDays;
                        employeeService.updateEmployeeAvailablePaidLeave(token, userId, newAvailablePaidLeave);
                    }
                }

            } else if (result.getValue().equals("Invalid or expired token.")) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            } else{
                System.out.println("Failed to process leave: " + result.getValue());
            }

            return true;

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public boolean reject(){
        String token = Token.getDecodedToken().getTokenString();
        try{
            int leaveId = -1;

            while (true) {
                System.out.println("Enter the Leave ID of the leave application you want to reject: ");
                if (Main.scanner.hasNextInt()) {
                    leaveId = Main.scanner.nextInt();  
                    Main.scanner.nextLine(); 
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid Leave ID (integer).");
                    Main.scanner.nextLine(); 
                }
            }

            AbstractMap.Entry<Boolean, String> result = leaveApplicationService.processLeaveApplication(token, leaveId, "Rejected");

            if (result.getKey()) {
                System.out.println("Leave application rejected successfully!");
            } else if (result.getValue().equals("Invalid or expired token.")) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            } else{
                System.out.println("Failed to process leave: " + result.getValue());
            }

            return true;

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } 
    }
    
    
    private String truncate(String value, int length) {
        if (value == null) {
            return ""; 
        } else if (value.length() <= length) {
            return value;
        } else {
            return value.substring(0, length - 3) + "..."; // Adds "..." to truncated strings
        }
    }
    
    


}
