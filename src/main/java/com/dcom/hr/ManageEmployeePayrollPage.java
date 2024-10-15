package com.dcom.hr;

import com.dcom.Main;
import com.dcom.rmi.PayrollService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;

import java.io.File;
import java.rmi.RemoteException;
import com.dcom.dataModel.Payroll;
import com.dcom.dataModel.PayrollTemplate;
import com.dcom.utils.AsciiArt;
import com.dcom.utils.PayrollPDFExporter;

import java.time.LocalDate;
import java.util.List;

public class ManageEmployeePayrollPage {
    
    private PayrollService payrollService;

    public ManageEmployeePayrollPage() {
        try {
            payrollService = ServiceLocator.getPayrollService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(){

        if(payrollService == null) {
            System.out.println("Connection to server failed................");
            return;
        }

        int choice = -1;

        while (true) {
            System.out.println("========= Manage Employee Payroll =========");
            System.out.println("1. View All");
            System.out.println("2. Process Payroll");
            System.out.println("3. Generate Payroll Invoice");
            System.out.println("4. Export Payroll Invoice");
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
                    processPayroll();
                    break;
                case 3:
                    generatePayrollInvoice();
                    break;
                case 4:
                    exportPayrollInvoice();
                    break;
                case 0:
                    System.out.println("Going back...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again...........");
            }

            AsciiArt.printDivider();
        }
    }

    private void viewAll() {
        String token = Token.getDecodedToken().getTokenString();
        try {
            List<Payroll> payrolls = payrollService.getPayrollList(token);
    
            if (payrolls != null && !payrolls.isEmpty()) {
                
                payrolls.sort((a, b) -> Integer.compare(a.getUserId(), b.getUserId()));
    
                printTableHeader();
    
                for (int i = 0; i < payrolls.size(); i++) {
                    printPayrollRow(payrolls.get(i));
                    if (i < payrolls.size() - 1) {
                        printRowDivider();
                    }
                }
    
                printTableFooter();
            } else {
                System.out.println("No payroll records found.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    private void printTableHeader() {
        System.out.println("┌────────────┬──────────┬─────────────┬───────┬──────────┬──────────────┬──────────┬────────────┐");
        System.out.printf("│ %-10s │ %-8s │ %-11s │ %-5s │ %-8s │ %-12s │ %-8s │ %-10s │%n",
                "Payroll ID", "User ID", "Total Paid", "Paid", "Tax", "Salary Class", "EPF", "Date");
        System.out.println("├────────────┼──────────┼─────────────┼───────┼──────────┼──────────────┼──────────┼────────────┤");
    }
    
    private void printPayrollRow(Payroll payroll) {
        System.out.printf("│ %-10d │ %-8d │ %-11.2f │ %-5s │ %-8.2f │ %-12s │ %-8.2f │ %-10s │%n",
                payroll.getPayrollId(),
                payroll.getUserId(),
                payroll.getTotalPaid(),
                payroll.isPaid() ? "Yes" : "No",
                payroll.getTax(),
                payroll.getSalaryClass(),
                payroll.getEpf(),
                payroll.getDate().toString()); 
    }
    
    private void printRowDivider() {
        System.out.println("├────────────┼──────────┼─────────────┼───────┼──────────┼──────────────┼──────────┼────────────┤");
    }
    
    private void printTableFooter() {
        System.out.println("└────────────┴──────────┴─────────────┴───────┴──────────┴──────────────┴──────────┴────────────┘");
    }
    
    
    private void processPayroll(){
        String token = Token.getDecodedToken().getTokenString();
        try{
            int payrollId = -1;

            while (true) {
                System.out.println("Enter the Payroll ID of the employee you want to process: ");
                if (Main.scanner.hasNextInt()) {
                    payrollId = Main.scanner.nextInt();  
                    Main.scanner.nextLine();  // Consume the newline character after the integer input
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid Payroll ID (integer).");
                    Main.scanner.nextLine(); 
                }
            }

            boolean processed = payrollService.processPayroll(token, payrollId);

            if (processed) {
                System.out.println("Payroll processed successfully!");
            } else {
                System.out.println("Payroll not found.");
            }
        } catch (RemoteException e) {
            System.err.println("Error occurred while processing payroll. Please try again later.");
            e.printStackTrace();
        }
    }
    

    private void generatePayrollInvoice() {
        String token = Token.getDecodedToken().getTokenString();
        try {
            int payrollId = -1;

            while (true) {
                System.out.println("Enter the Payroll ID you want to generate invoice for: ");
                if (Main.scanner.hasNextInt()) {
                    payrollId = Main.scanner.nextInt();
                    Main.scanner.nextLine(); 
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid Payroll ID (integer).");
                    Main.scanner.nextLine();
                }
            }

            
            List<Payroll> payrolls = payrollService.getPayrollList(token);
            Payroll matchingPayroll = null;
            int employeeUserId = -1;

            if (payrolls != null && !payrolls.isEmpty()) {
                for (Payroll payroll : payrolls) {
                    if (payroll.getPayrollId() == payrollId) {
                        employeeUserId = payroll.getUserId();  
                        matchingPayroll = payroll;
                        break;  
                    }
                }
            }

            if (matchingPayroll == null) {
                System.out.println("Payroll with ID " + payrollId + " not found.");
                return;
            }

            PayrollTemplate invoice = payrollService.generatePayroll(token, payrollId, employeeUserId);

            if (invoice != null) {
                LocalDate paymentDate = LocalDate.now();

                System.out.println("======================================");
                System.out.println("            PAYROLL INVOICE            ");
                System.out.println("======================================");
                System.out.println();
                System.out.printf("Employee Name: %-20s%n", invoice.getEmployeeName());
                System.out.printf("Pay Period  :    %-20s%n", invoice.getPayPeriod());
                System.out.printf("Generate On :    %-20s%n", paymentDate.toString());
                System.out.println();
                System.out.println("EARNINGS:");
                System.out.println("--------------------------------------");
                System.out.printf("Base Salary:     $%8.2f%n", invoice.getBaseSalary());
                System.out.printf("Total Earned:    $%8.2f%n", invoice.getTotalEarned());
                System.out.println();
                System.out.println("DEDUCTIONS:");
                System.out.println("--------------------------------------");
                System.out.printf("Tax:             $%8.2f%n", invoice.getTax());
                System.out.printf("EPF:             $%8.2f%n", invoice.getEpf());
                System.out.printf("Leave Deduction: $%8.2f%n", invoice.getLeaveWithoutPayDeduction());
                System.out.printf("Total Deduction: $%8.2f%n", invoice.getTotalDeductions());
                System.out.println();
                System.out.printf("NET PAY:         $%8.2f%n", invoice.getNetPay());
                System.out.println();
                System.out.println("Paid:            " + (invoice.isPaid() ? "Yes" : "No"));
                System.out.println("======================================");
            } else {
                System.out.println("Failed to generate payroll invoice.");
            }

            System.out.println("Export this payroll to PDF? (Y/N): ");
            String exportChoice = Main.scanner.nextLine().trim().toUpperCase();

            if (exportChoice.equals("Y")) {
                File reportDir = new File("Payslip");
                if (!reportDir.exists()) {
                    reportDir.mkdir();
                }
                PayrollPDFExporter exporter = new PayrollPDFExporter(invoice, "Payslip\\payroll_invoice_" + payrollId + ".pdf", "logo.png");
                Thread exportThread = new Thread(exporter);
                
                exportThread.start();
                try {
                    exportThread.join();
                    System.out.println("Payroll invoice exported successfully.");
                } catch (InterruptedException e) {
                    System.err.println("Error occurred while exporting payroll invoice. Please try again later.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Export cancelled.");
            }

        } catch (RemoteException e) {
            System.err.println("Error occurred while generating payroll invoice. Please try again later.");
            e.printStackTrace();
        }
    }

    
    private void exportPayrollInvoice(){
        String token = Token.getDecodedToken().getTokenString();
        try {
            int payrollId = -1;

            while (true) {
                System.out.println("Enter the Payroll ID you want to generate invoice for: ");
                if (Main.scanner.hasNextInt()) {
                    payrollId = Main.scanner.nextInt();
                    Main.scanner.nextLine(); 
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid Payroll ID (integer).");
                    Main.scanner.nextLine();
                }
            }

            
            List<Payroll> payrolls = payrollService.getPayrollList(token);
            Payroll matchingPayroll = null;
            int employeeUserId = -1;

            if (payrolls != null && !payrolls.isEmpty()) {
                for (Payroll payroll : payrolls) {
                    if (payroll.getPayrollId() == payrollId) {
                        employeeUserId = payroll.getUserId();  
                        matchingPayroll = payroll;
                        break;  
                    }
                }
            }

            if (matchingPayroll == null) {
                System.out.println("Payroll with ID " + payrollId + " not found.");
                return;
            }

            PayrollTemplate invoice = payrollService.generatePayroll(token, payrollId, employeeUserId);

            if (invoice != null) {
                File reportDir = new File("Payslip");
                if (!reportDir.exists()) {
                    reportDir.mkdir();
                }
                PayrollPDFExporter exporter = new PayrollPDFExporter(invoice, "Payslip\\payroll_invoice_" + payrollId + ".pdf", "logo.png");
                Thread exportThread = new Thread(exporter);
                
                exportThread.start();
                try {
                    exportThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to generate payroll invoice.");
            }

        } catch (RemoteException e) {
            System.err.println("Error occurred while exporting payroll invoice. Please try again later.");
            e.printStackTrace();
        }
    }
   
    
    
    
    
    


}
