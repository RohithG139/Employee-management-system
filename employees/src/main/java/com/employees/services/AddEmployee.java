package com.employees.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.model.Employee;
import com.employees.security.Roles;
import com.employees.utils.IdGenerator;
import com.employees.utils.PasswordGenerator;
import com.employees.utils.Util;

public class AddEmployee {
	public void insert() throws IOException, ParseException {
		Scanner sc = new Scanner(System.in);
		EmployeeDao dao = new EmployeeDaoImpl();
		IdGenerator genObj = new IdGenerator();
		String id = genObj.getId();

		System.out.println("Enter name:");
		String name = sc.next();

		System.out.println("Enter dept:");
		String dept = sc.next();
		String email;
		while (true) {
			try {
				System.out.println("Enter email:");
				email = sc.next();
				Util.validateEmail(email);
				break;
			} catch (IllegalEmailException e) {
				System.out.println(e.getMessage());
			}
		}
		String phnNo;
		while (true) {
			try {
				System.out.println("Enter phnNo:");
				phnNo = sc.next();
				Util.validatePhnNo(phnNo);
				break;
			} catch (IllegalPhnNoException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Enter roles and exit to finish");
		Set<Roles> roles = new HashSet<>();
		while (true) {
			System.out.println("Available Roles:");
			for (Roles role : Roles.values()) {
				System.out.println(role);
			}
			try {
			System.out.println("Enter role:");
			String input = sc.next().toUpperCase();
			
			if (input.equals("EXIT")) {
				break;
			}
			Roles role = Roles.valueOf(input);
			
			if (roles.add(role)) {
				System.out.println("Role added successfully");
			} else {
				System.out.println("Role already added");
			}
			}catch(IllegalArgumentException e) {
				System.out.println("Provide valid role");
				continue;
			}
		}

		String password = "Tek@" + PasswordGenerator.generate();
		String hashedPassword = Util.hashPassword(password);

		dao.addEmployee(new Employee(id, name, dept, email, phnNo, roles, hashedPassword));
		System.out.println("Employee added succesfully");
		System.out.println("Your password:" + password);

	}
}
