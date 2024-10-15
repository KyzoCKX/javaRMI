package com.dcom.utils;

import java.util.Scanner;

import com.dcom.hr.HRMenu;
//import com.dcom.employee.EmployeePortal;
public class Navigator {

    public static void navigateToHRPortal() {
        HRMenu hrMenu = new HRMenu();
        hrMenu.showMenu();  // Show HR menu after successful login
    }

//    public static void navigateToEmployeePortal() {
//        EmployeePortal employeePortal = new EmployeePortal();
//        employeePortal.showMenu();  // Show Employee menu after successful login
//    }
}
