package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.security.ValidateLogin;
import com.employees.utils.ValidatorID;

public class PasswordOperations {
	EmployeeDao dao=new EmployeeDaoImpl();
	ValidateLogin loginObj=new ValidateLogin();
	Scanner sc=new Scanner(System.in);
	public void resetPassword() throws ParseException, IOException {
		
		System.out.println("Enter id for resetting password:");
		String id=sc.next().toUpperCase();
		if(!ValidatorID.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		dao.resetPassword(id);
		System.out.println("Password reset successfully");
	}
	public void changePassword() throws ParseException, IOException{
		String id=loginObj.id;
		System.out.println("Enter new password:");
		String newPass=sc.next();
		dao.changePassword(id,newPass);
		System.out.println("password is changed Successfully.");
	}
}
