package com.employees.services;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import com.employees.dao.EmployeeDao;
import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.model.Employee;
import com.employees.security.Roles;
import com.employees.utils.Util;

public class AddEmployee {

    public void insert(EmployeeDao dao) {
        Scanner sc = new Scanner(System.in);
        
        
        String name = "";
        while (true) {
            System.out.println("Enter name:");
            name = sc.nextLine();
            if (Util.validateName(name)) {
                break;
            }
            System.out.println("Invalid name. Name cannot be empty.");
        }

        
        String dept = "";
        while (true) {
            System.out.println("Enter dept:");
            dept = sc.nextLine();
            if (Util.validateDept(dept)) {
                break;
            }
            System.out.println("Invalid department. Dept cannot be empty.");
        }

        
        String email = "";
        while (true) {
            try {
                System.out.println("Enter email:");
                email = sc.nextLine();
                Util.validateEmail(email);
                break;
            } catch (IllegalEmailException e) {
                System.out.println("Invalid email:"+e.getMessage());
            }
        }

        
        String phnNo = "";
        while (true) {
            try {
                System.out.println("Enter phnNo:");
                phnNo = sc.nextLine();
                Util.validatePhnNo(phnNo);
                break;
            } catch (IllegalPhnNoException e) {
                System.out.println("Invalid phnNo:"+e.getMessage());
            }
        }

      
        Set<Roles> roles = new HashSet<>();
        while (true) {
            System.out.println("\nAvailable Roles:");
            for (Roles role : Roles.values()) {
                System.out.println(role);
            }
            System.out.println("Enter role or type exit to finish:");
            String input = sc.nextLine().toUpperCase().trim();

            if (input.equals("EXIT")) {
                if (roles.isEmpty()) {
                    System.out.println("Please select at least one role.");
                    continue;
                }
                break;
            }

            try {
                Roles role = Roles.valueOf(input);
                if (roles.add(role)) {
                    System.out.println("Role [" + role + "] added successfully.");
                } else {
                    System.out.println("Role already exists in list.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please provide a valid role from the list.");
            }
        }
        String password = "Tek@" + Util.generatePassword();
        String hashedPassword = Util.hashPassword(password);

        Employee newEmp = new Employee(name, dept, email, phnNo, roles, hashedPassword);
        dao.addEmployee(newEmp);

        System.out.println("\nEmployee added successfully!");
        System.out.println("Temporary Password: " + password);
        
        dao.fetchEmployee();
    }
}
