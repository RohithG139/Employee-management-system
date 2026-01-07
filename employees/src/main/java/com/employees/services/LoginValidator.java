package com.employees.services;

import java.util.Scanner;
import java.util.Set;

import com.employees.dao.EmployeeDaoImpl;

public class LoginValidator {
	Scanner sc = new Scanner(System.in);
	EmployeeDaoImpl dao=new EmployeeDaoImpl();
	public boolean validate() {
		boolean validUser = false;
		while (!validUser) {
			System.out.println("Enter username:");
			String name = sc.next();
			System.out.println("Enter password");
			String password = sc.next();
			validUser=dao.validateUser(name, password);
			if(validUser) {
				return true;
			}
			else {
				System.out.println("Invalid credintials");
				continue;
			}
		}
		return false;
	}
}
