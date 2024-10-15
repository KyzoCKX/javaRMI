package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.LeaveApplicationService;
import com.dcom.rmi.EmployeeManagementService;
import com.dcom.rmi.PayrollService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;

import java.io.File;
import java.rmi.RemoteException;
import com.dcom.dataModel.LeaveApplication;
import com.dcom.dataModel.Employee;
import com.dcom.dataModel.Payroll;
import com.dcom.utils.AsciiArt;
import com.dcom.utils.EmployeePDFExporter;
import com.dcom.utils.LeaveApplicationPDFExporter;
import com.dcom.utils.AllPayrollPDFExporter;

import java.time.LocalDate;
import java.util.List;

public class GenerateReportPage {
    
    private LeaveApplicationService leaveApplicationService;
    private EmployeeManagementService employeeService;
    private PayrollService payrollService;

    public GenerateReportPage() {
        try {
            leaveApplicationService = ServiceLocator.getLeaveApplicationService();
            employeeService = ServiceLocator.getEmployeeManagementService();
            payrollService = ServiceLocator.getPayrollService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(){
            
            if(employeeService == null || leaveApplicationService == null || payrollService == null) {
                System.out.println("Connection to server failed................");
                return;
            }
    
            int choice = -1;
    
            while (true) {
                AsciiArt.printDivider();
                System.out.println("======== Generate Report =========");
                System.out.println("1. Export Employee Report");
                System.out.println("2. Export Leave Application Report");
                System.out.println("3. Export Payroll Report");
                System.out.println("4. Export All");
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
                        boolean result1 = exportEmployeeReport();
                        if(!result1) {
                            return;
                        }
                        break;
                    case 2:
                        boolean result2 = exportLeaveApplicationReport();
                        if(!result2) {
                            return;
                        }
                        break;
                    case 3:
                        boolean result3 = exportPayrollReport();
                        if(!result3) {
                            return;
                        }
                        break;
                    case 4:
                        boolean result4 = exportAll();
                        if(!result4) {
                            return;
                        }
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.......");
                }
            }
    }

    private boolean exportEmployeeReport(){
        try {
            String token = Token.getDecodedToken().getTokenString();
            List<Employee> employees = employeeService.retrieveAllEmployee(token);
            if(employees == null) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            }
            File reportDir = new File("Report");
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
            EmployeePDFExporter exporter = new EmployeePDFExporter(employees, "Report\\EmployeeReport_" + LocalDate.now().toString() + ".pdf");
            Thread thread = new Thread(exporter);
            thread.start();
            try{
                thread.join();
                System.out.println("Employee Report exported successfully.");
            } catch (InterruptedException e) {
                System.out.println("Employee Report export failed.");
                e.printStackTrace();
            }
            

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean exportLeaveApplicationReport(){
        try {
            String token = Token.getDecodedToken().getTokenString();
            List<LeaveApplication> leaveApplications = leaveApplicationService.retrieveLeaveApplicationForAllEmployee(token);
            if(leaveApplications == null) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            }
            File reportDir = new File("Report");
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
            LeaveApplicationPDFExporter exporter = new LeaveApplicationPDFExporter(leaveApplications, "Report\\LeaveApplicationReport_" + LocalDate.now().toString() + ".pdf");
            Thread thread = new Thread(exporter);
            thread.start();
            try{
                thread.join();
                System.out.println("Leave Application Report exported successfully.");
            } catch (InterruptedException e) {
                System.out.println("Leave Application Report export failed.");
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean exportPayrollReport(){
        try {
            String token = Token.getDecodedToken().getTokenString();
            List<Payroll> payrolls = payrollService.getPayrollList(token);
            if(payrolls == null) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            }
            File reportDir = new File("Report");
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
            AllPayrollPDFExporter exporter = new AllPayrollPDFExporter(payrolls, "Report\\PayrollReport_" + LocalDate.now().toString() + ".pdf");
            Thread thread = new Thread(exporter);
            thread.start();
            try{
                thread.join();
                System.out.println("Payroll Report exported successfully.");
            } catch (InterruptedException e) {
                System.out.println("Payroll Report export failed.");
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean exportAll() {
        try {
            String token = Token.getDecodedToken().getTokenString();
    
            List<Employee> employees = employeeService.retrieveAllEmployee(token);
            List<LeaveApplication> leaveApplications = leaveApplicationService.retrieveLeaveApplicationForAllEmployee(token);
            List<Payroll> payrolls = payrollService.getPayrollList(token);
    
            if (employees == null || leaveApplications == null || payrolls == null) {
                System.out.println("Token Expired.");
                Token.clearToken();
                Token.deleteTokenFile("tokenFile.dat");
                return false;
            }

            File reportDir = new File("Report");
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
    
            EmployeePDFExporter employeeExporter = new EmployeePDFExporter(employees, "Report\\EmployeeReport_" + LocalDate.now().toString() + ".pdf");
            LeaveApplicationPDFExporter leaveApplicationExporter = new LeaveApplicationPDFExporter(leaveApplications, "Report\\LeaveApplicationReport_" + LocalDate.now().toString() + ".pdf");
            AllPayrollPDFExporter payrollExporter = new AllPayrollPDFExporter(payrolls, "Report\\PayrollReport_" + LocalDate.now().toString() + ".pdf");
    
            Thread employeeThread = new Thread(employeeExporter);
            Thread leaveApplicationThread = new Thread(leaveApplicationExporter);
            Thread payrollThread = new Thread(payrollExporter);
    
            employeeThread.start();
            leaveApplicationThread.start();
            payrollThread.start();
    
            employeeThread.join();
            leaveApplicationThread.join();
            payrollThread.join();
    
            System.out.println("All reports exported successfully.");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Report export interrupted.");
            e.printStackTrace();
        }
        return true;
    }
           
    
    

}