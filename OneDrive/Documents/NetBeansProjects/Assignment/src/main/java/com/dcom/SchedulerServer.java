
package com.dcom;
import com.dcom.dataModel.Payroll;
import com.dcom.dataModel.Employee;
import com.dcom.dataModel.SalaryClass;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerServer {
    private DataRetrievalInterface databaseService;

    public SchedulerServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            databaseService = (DataRetrievalInterface) registry.lookup("Server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every day
        scheduler.scheduleAtFixedRate(() -> {
            LocalDate today = LocalDate.now();
            
            // Check if today is the first day of the month
            if (today.getDayOfMonth() == 1) {
                createPayrollForAllUsers();
            }

            // Check if today is the first day of the year (January 1st)
            if (today.getDayOfYear() == 1) {
                resetLeaveAndPaidLeaveForEmployees();
            }

        }, 0, 1, TimeUnit.DAYS); // Check daily
    }

    // 2. Create payroll for all users on the first day of every month
    private void createPayrollForAllUsers() {
        try {
            List<Employee> employees = databaseService.retrieveAllEmployee(); 
            for (Employee employee : employees) {
                double salary = employee.getSalary(); 
            if (salary == 0) {
                continue; 
            }
            SalaryClass salaryClass = databaseService.getSalaryClassBySalary(salary);
            
                double tax = salary * salaryClass.getTaxPercentage(); // tax that need to be deduct
                double epf = salary * salaryClass.getEpfPercentage();
                double totalPaid = salary - tax - epf;

                Payroll payroll = new Payroll(
                        employee.getUserId(),
                        //payroll autogenerate
                        totalPaid,
                        false, // paid status initially false
                        tax,
                        salaryClass.getSalaryClass(), // Assuming salary class is "A" or it can be dynamic
                        epf, // Assuming percentage 10
                        java.sql.Date.valueOf(LocalDate.now()) // Today's date
                );
                
                // Call RMI service to create payroll
                databaseService.createPayroll(payroll); 
                System.out.println("Payroll created for user ID: " + employee.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Reset leave applications and update available paid leave on the first day of the year
    private void resetLeaveAndPaidLeaveForEmployees() {
        try {
            // Reset leave_application table
            databaseService.resetLeaveApplications(); 

            // Reset available paid leave for all employees
            List<Employee> employees = databaseService.retrieveAllEmployee(); 
            for (Employee employee : employees) {
                if(employee.getAvailablePaidLeave() == 10){
                    continue;
                }
                employee.setAvailablePaidLeave(10); // Reset available paid leave to 10
                databaseService.updateEmployee(employee); // Update employee's leave
                System.out.println("Available paid leave reset for employee ID: " + employee.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SchedulerServer schedulerClient = new SchedulerServer();
        schedulerClient.startScheduler();
    }
}
