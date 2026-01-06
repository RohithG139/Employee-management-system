package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.security.ValidateLogin;
import com.employees.utils.Util;
import com.employees.utils.ValidatorID;

public class UpdateEmployee {
	Scanner sc=new Scanner(System.in);
	EmployeeDao dao=new EmployeeDaoImpl();
	ValidateLogin loginObj=new ValidateLogin();
  public void update() throws IOException,ParseException{
	  
	    System.out.println("Enter Id to Update:");
		String id=sc.next().toUpperCase();
		if(!ValidatorID.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		System.out.println("Enter new name to Update:");
		String newName=sc.next();
		dao.updateEmployee(id,newName);
		System.out.println("Employee updated Successfully");
  }
  
  public void updateUserLogin() throws IOException,ParseException{
	  String id=loginObj.id;
	  String phnNo;
	  while (true) {
			try {
				System.out.println("Enter new phnNo:");
				phnNo=sc.next();
				Util.validatePhnNo(phnNo);
				break;
			} catch (IllegalPhnNoException e) {
				System.out.println(e.getMessage());
			}
		}
	  System.out.println("Enter new email");
	  String newEmail=sc.next();
	  dao.updateUserLogin(id,phnNo,newEmail);
	  System.out.println("Updated succesfully");
  }
}
