package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.model.Employee;
import com.employees.utils.Util;

public class AddEmployee {
	public void insert() throws IOException, ParseException {
		Scanner sc = new Scanner(System.in);
		EmployeeDao dao = new EmployeeDaoImpl();
		GenerateId genObj=new GenerateId();
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
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();

		System.out.println("Enter password:");
		String password = sc.next();

		dao.addEmployee(new Employee(id, name, dept, email, phnNo, role, password));
		System.out.println("Employee added succesfully");

	}
}
