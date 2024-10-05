package com.database;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.io.ObjectInputStream;

public class Server{

    public static void main(String[] args) {
        Connection con = PostgreCon.getConnection();

        try {
            RmiService dataService = new RmiServiceImplement(con);
            Registry registry = LocateRegistry.createRegistry(1099); // Default RMI port
            registry.rebind("DatabaseService", dataService);
            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
//        Connection con = PostgreCon.getConnection();
//        try (ServerSocket serverSocket = new ServerSocket(12345)) {
//            System.out.println("Server is running and waiting for a connection...");
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("Client connected.");
//            // Create input/output streams
//            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
//            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
//
//            // Read the request type from the client (either "user" or "employee")
//            String requestType = (String) reader.readObject();
//            int userId = reader.readInt(); // Assume client sends userId after request type
//
//            // Respond based on the request type
//            if ("user".equalsIgnoreCase(requestType)) {
//                User retrievedUser = User.getUser(con, userId);
//                writer.writeObject(retrievedUser);  // Send User object to the client
//            } else if ("employee".equalsIgnoreCase(requestType)) {
//                Employee employee = Employee.getEmployeeById(con, userId);
//                writer.writeObject(employee);  // Send Employee object to the client
//            }
//
//            writer.flush();
//            System.out.println("Data sent to client based on request: " + requestType);
//
//            writer.close();
//            reader.close();
//            clientSocket.close();
//            // Wait for client connection
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("Client connected.");
//            
//            User retrievedUser = User.getUser(con, 1);
//            Employee employee = null;
//            if (retrievedUser != null) {
//                 employee = Employee.getEmployeeById((con), retrievedUser.getUserId());
//                 System.err.println(employee.getUserId());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //create new User
//        int generatedUserId = User.createUser(con, new User("password789", "inactive", "employee"));
//        if (generatedUserId != -1) {
//          Employee.createEmployee(con, generatedUserId);
//            System.out.println("User inserted successfully with userId: " + generatedUserId);
//        }
//        // Retrieve the user
//        User retrievedUser = User.getUser(con, generatedUserId);
//        if (retrievedUser != null) {
//            System.out.println("User retrieved: " + retrievedUser.getPwd());
//        }
//        // Update the user
//        retrievedUser.setStatus("active");
//        User.updateUser(con, retrievedUser);
//        // Delete the user
//        User.deleteUser(con, generatedUserId);

        // Retrieve an employee by userId
        
//        Employee employee = Employee.getEmployeeById(con, 2); // Assuming 2 is a valid userId
//        if (employee != null) {
//            System.out.println("Employee Name: " + employee.getName());
//            System.out.println("Salary: " + employee.getSalary());
//        }
//
//        // Update employee salary
//        Employee.updateEmployee(con, 2, "name",55000.00); // Update salary for userId 2
