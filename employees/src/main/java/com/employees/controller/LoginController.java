package com.employees.controller;

import java.util.Scanner;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.services.LoginValidator;
import com.employees.services.PasswordOperations;

public class LoginController {
	private static final Scanner sc = new Scanner(System.in);
	PasswordOperations passwordOperations = new PasswordOperations();
	LoginValidator validator = new LoginValidator();

	public LoginResult validateUser(EmployeeDao dao) {

		while (true) {
			System.out.println("Enter id:");
			String id = sc.next().toUpperCase();
			System.out.println("Enter password");
			String password = sc.next();

			try {
				LoginResult loggedInUser = validator.login(dao, id, password);
				System.out.println("Employee login succesfully");
				return loggedInUser;
			} catch (ValidationException e) {
				System.out.println("error during login:" + e.getMessage());
			}
		}

	}

	public void changePassword(EmployeeDao dao) {
		String id = Menu.currentUser.getEmpId();

		System.out.println("Enter new password:");
		String password = sc.next();
		try {
			passwordOperations.changePasswordService(dao,id,password);
			System.out.println("Password changed succesfully");
			System.out.println("Your changed password:" + password);
		}catch(ValidationException e) {
			System.out.println("error while change the password:"+e.getMessage());
		}catch(EmployeeNotFoundException e) {
			System.out.println("error:"+e.getMessage());
		}
	}

	public void resetPassword(EmployeeDao dao) {
		System.out.println("Enter id for resetting password:");
		String id = sc.next().toUpperCase();
		try {
			String newPassword=passwordOperations.resetPasswordService(dao,id);
			System.out.println("Password reset succesfully");
			System.out.println("Your reset password:" + newPassword);
		}catch(ValidationException e) {
			System.out.println("error while reset the password:"+e.getMessage());
		}catch(EmployeeNotFoundException e) {
			System.out.println("error:"+e.getMessage());
		}
	}

}
