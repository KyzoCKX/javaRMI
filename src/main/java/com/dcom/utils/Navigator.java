package com.dcom.utils;

import java.util.Scanner;

import com.dcom.hr.HRMenu;
import com.dcom.employee.EmployeeMenu;
public class Navigator {

    public static void navigateToHRPortal() {
        HRMenu hrPortal = new HRMenu();
        hrPortal.showMenu();  // Show HR menu after successful login
    }

   public static void navigateToEmployeePortal() {
       EmployeeMenu employeePortal = new EmployeeMenu();
       employeePortal.showMenu();  // Show Employee menu after successful login
   }
}
