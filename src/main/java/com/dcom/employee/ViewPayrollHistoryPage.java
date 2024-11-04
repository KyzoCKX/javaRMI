package com.dcom.employee;

import com.dcom.rmi.PayrollService;
import com.dcom.serviceLocator.ServiceLocator;
import com.dcom.utils.Token;

import java.rmi.RemoteException;
import java.util.Map;

public class ViewPayrollHistoryPage {

    private PayrollService payrollService;

    public ViewPayrollHistoryPage() {
        try {
            payrollService = ServiceLocator.getPayrollService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if (payrollService == null) {
            System.out.println("Connection to payroll service failed.");
            return;
        }

        String token = Token.getDecodedToken().getTokenString();
        try {
            Map<Integer, String> payrollMap = payrollService.getPayrollListForUser(token);
            if (payrollMap != null && !payrollMap.isEmpty()) {
                System.out.println("\n--- Payroll History ---");
                printPayrollHeader();
                for (Map.Entry<Integer, String> entry : payrollMap.entrySet()) {
                    printPayrollRow(entry);
                }
                printPayrollFooter();
            } else {
                System.out.println("No payroll records found.");
            }
        } catch (RemoteException e) {
            System.out.println("Error retrieving payroll history.");
            e.printStackTrace();
        }
    }

    private void printPayrollHeader() {
        System.out.println("┌────────────┬────────────────────────────────────────┐");
        System.out.printf("│ %-10s │ %-30s │%n", "Payroll ID", "Details");
        System.out.println("├────────────┼────────────────────────────────────────┤");
    }

    private void printPayrollRow(Map.Entry<Integer, String> entry) {
        System.out.printf("│ %-10d │ %-30s │%n", entry.getKey(), entry.getValue());
    }

    private void printPayrollFooter() {
        System.out.println("└────────────┴────────────────────────────────────────┘");
    }
}
