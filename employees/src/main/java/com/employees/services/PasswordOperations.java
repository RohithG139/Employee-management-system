package com.employees.services;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.utils.Util;

public class PasswordOperations {
	
	Scanner sc = new Scanner(System.in);

	public void resetPassword(EmployeeDao dao) {

		System.out.println("Enter id for resetting password:");
		String id = sc.next().toUpperCase();
		if (!Util.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		String password = "Tek@" + Util.generate();
		System.out.println("Your reset password:" + password);
		String hashedPassword = Util.hashPassword(password);
		dao.resetPassword(id, hashedPassword);
		System.out.println("Password reset successfully");
	}

	public void changePassword(EmployeeDao dao){
		 String id = Menu.currentUser.getEmpId();

		System.out.println("Enter new password:");
		String newPass = sc.next();
		dao.changePassword(id, Util.hashPassword(newPass));
		System.out.println("password is changed Successfully.");
	}
}
