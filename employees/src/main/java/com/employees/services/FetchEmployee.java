package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.security.Roles;
import com.employees.security.ValidateLogin;
import com.employees.utils.ValidatorID;

public class FetchEmployee {
	public void fetchAll() throws ParseException, IOException {
		EmployeeDao dao = new EmployeeDaoImpl();
		dao.fetchEmployee();
	}

	public void fetchById() throws ParseException, IOException {
		EmployeeDao dao = new EmployeeDaoImpl();
		Scanner sc = new Scanner(System.in);
		String id;
		if (ValidateLogin.roles.contains(Roles.EMPLOYEE)) {
			id = ValidateLogin.id;
		}

		else {
			System.out.println("Enter Employee id:");
			id = sc.next().toUpperCase();
			if (!ValidatorID.validId(id)) {
				System.out.println("please Enter valid id");
				return;
			}
		}
		dao.fetchEmployeeById(id);

	}
}
