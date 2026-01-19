package com.employees.services;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.model.Employee;
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

			System.out.println("Enter email:");
			email = sc.nextLine();
			if (Util.validateEmail(email))
				break;
			System.out.println("Invalid email format");

		}

		String phnNo = "";
		while (true) {

			System.out.println("Enter phnNo:");
			phnNo = sc.nextLine();
			if (Util.validatePhnNo(phnNo))
				break;
			System.out.println("Invalid phnNo format");
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

			if (!Util.validateRole(input)) {
		        System.out.println("Invalid role,choose from available roles");
		        continue;
		    }

		    Roles role = Roles.valueOf(input.toUpperCase());

		    if (roles.add(role)) {
		        System.out.println("Role [" + role + "] added successfully.");
		    } else {
		        System.out.println("Role already exists.");
		    }
		}
		String password = "Tek@" + Util.generatePassword();
		String hashedPassword = Util.hashPassword(password);

		Employee newEmp = new Employee(name, dept, email, phnNo, roles, hashedPassword);
		dao.addEmployee(newEmp);

		System.out.println("Employee added successfully!");
		System.out.println("Temporary Password: " + password);

		dao.fetchEmployee();
	}
}
