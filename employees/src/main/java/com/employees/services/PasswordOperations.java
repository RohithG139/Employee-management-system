package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.utils.PasswordGenerator;
import com.employees.utils.Util;
import com.employees.utils.IdValidator;

public class PasswordOperations {
	EmployeeDao dao=new EmployeeDaoImpl();
	//LoginValidator loginObj=new LoginValidator();
	Scanner sc=new Scanner(System.in);
	public void resetPassword() throws ParseException, IOException {
		
		System.out.println("Enter id for resetting password:");
		String id=sc.next().toUpperCase();
		if(!IdValidator.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		String password="Tek@"+PasswordGenerator.generate();
		System.out.println("Your reset password:"+password);
		String hashedPassword=Util.hashPassword(password);
		dao.resetPassword(id,hashedPassword);
		System.out.println("Password reset successfully");
	}
	public void changePassword() throws ParseException, IOException{
		String id=EmployeeDaoImpl.id;
		System.out.println("Enter new password:");
		String newPass=sc.next();
		dao.changePassword(id,Util.hashPassword(newPass));
		System.out.println("password is changed Successfully.");
	}
}
