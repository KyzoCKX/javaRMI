/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.database;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        try {
            // Locate the registry on the server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Change "localhost" to server address if needed

            // Retrieve the DataService from the registry
            RmiService rmiService = (RmiService) registry.lookup("DatabaseService");


//            User user = rmiService.retrieveUser(2);
//            System.out.println("Retrieved User: " + user.getPwd());

//            List<Payroll> payrolls = rmiService.retrievePayrollByUserId(2);
//                for (Payroll payroll : payrolls) {
//                    System.out.println("Payroll ID: " + payroll.getPayrollId() + ", Total Paid: " + payroll.getTotalPaid());
//            }
                
            List<User> users = rmiService.retrieveAllUser();
                for (User user : users) {
                    System.out.println("User ID: " + user.getUserId()+ ", User Passwords " + user.getPwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//        try (Socket socket = new Socket("localhost", 12345)) {
//            System.out.println("Connected to the server.");
//
//            // Create input/output streams
//            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
//
//            // Get input from user to request either "user" or "employee"
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Enter request type (user/employee): ");
//            String requestType = scanner.nextLine();
//
//            System.out.print("Enter user ID: ");
//            int userId = scanner.nextInt();
//
//            // Send request type and userId to the server
//            writer.writeObject(requestType);
//            writer.writeInt(userId);
//            writer.flush();
//
//            // Receive the appropriate object (User or Employee) from the server
//            if ("user".equalsIgnoreCase(requestType)) {
//                User user = (User) reader.readObject();
//                if (user != null) {
//                    System.out.println("User ID: " + user.getUserId());
//                    System.out.println("Password: " + user.getPwd());
//                    System.out.println("Password: " + user.getUserType());
//                    System.out.println("Password: " + user.getStatus());
//                } else {
//                    System.out.println("User not found.");
//                }
//            } else if ("employee".equalsIgnoreCase(requestType)) {
//                Employee employee = (Employee) reader.readObject();
//                if (employee != null) {
//                    System.out.println("User ID: " + employee.getUserId());
//                    System.out.println("Name: " + employee.getName());
//                    System.out.println("Salary: " + employee.getSalary());
//                    System.out.println("Total Days of Work: " + employee.getTotalDaysOfWork());
//                    System.out.println("Available Paid Leave: " + employee.getAvailablePaidLeave());
//                } else {
//                    System.out.println("Employee not found.");
//                }
//            }
//
//            // Close resources
//            writer.close();
//            reader.close();
//            socket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

