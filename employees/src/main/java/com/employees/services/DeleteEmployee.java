package com.employees.services;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDao;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.utils.ValidatorID;

public class DeleteEmployee {
	public void delete() throws IOException,ParseException{
		Scanner sc=new Scanner(System.in);
		EmployeeDao dao=new EmployeeDaoImpl();
		System.out.println("Enter Id to Delete:");
		String id=sc.next().toUpperCase();
		if(!ValidatorID.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		
		dao.deleteEmployee(id);
		System.out.println("Employee deleted Successfully");
	}
}
