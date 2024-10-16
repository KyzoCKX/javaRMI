package com.dcom.serviceLocator;

import com.dcom.rmi.LoginService;
import com.dcom.rmi.UserService;
import com.dcom.rmi.EmployeeManagementService;
import com.dcom.rmi.LeaveApplicationService;
import com.dcom.rmi.PayrollService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceLocator {
    private static LoginService loginService;
    private static UserService userService;
    private static EmployeeManagementService employeeManagementService;
    private static LeaveApplicationService leaveApplicationService;
    private static PayrollService payrollService;

    private ServiceLocator() {}

    public static LoginService getLoginService() {
        if (loginService == null) {
            try {
                Registry authServiceRegistry = LocateRegistry.getRegistry("192.168.196.186", 8080);
                loginService = (LoginService) authServiceRegistry.lookup("loginService");
                // loginService = (LoginService)Naming.lookup("192.168.196.186:8080/loginService");
                userService = (UserService) authServiceRegistry.lookup("userService");
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return loginService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            try {
                Registry authServiceRegistry = LocateRegistry.getRegistry("192.168.196.186", 8080);
                userService = (UserService) authServiceRegistry.lookup("userService");
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return userService;
    }

    public static EmployeeManagementService getEmployeeManagementService() {
        if (employeeManagementService == null) {
            try {
                Registry employeeServiceRegistry = LocateRegistry.getRegistry("192.168.196.186", 8081);
                employeeManagementService = (EmployeeManagementService) employeeServiceRegistry.lookup("employeeManagementService");
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return employeeManagementService;
    }

    public static LeaveApplicationService getLeaveApplicationService() {
        if (leaveApplicationService == null) {
            try {
                Registry leaveApplicationServiceRegistry = LocateRegistry.getRegistry("192.168.196.186", 8083);
                leaveApplicationService = (LeaveApplicationService) leaveApplicationServiceRegistry.lookup("leaveApplicationService");
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return leaveApplicationService;
    }

    public static PayrollService getPayrollService() {
        if (payrollService == null) {
            try {
                Registry payrollServiceRegistry = LocateRegistry.getRegistry("192.168.196.186", 8082);
                payrollService = (PayrollService) payrollServiceRegistry.lookup("payrollService");
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return payrollService;
    }
}
