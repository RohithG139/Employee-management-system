package com.employees.services;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDaoImpl;

public class LoginValidator {
	Scanner sc = new Scanner(System.in);
	EmployeeDaoImpl dao=new EmployeeDaoImpl();
	public boolean validate() throws ParseException, IOException{
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
