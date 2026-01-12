package com.employees.services;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.model.Employee;
import com.employees.security.Roles;
import com.employees.utils.Util;

public class UpdateEmployee {
	Scanner sc = new Scanner(System.in);
	LoginValidator loginObj = new LoginValidator();
	public void update(EmployeeDao dao) {
		String id;
		Roles role;
		if (!Menu.currentUser.getRoles().contains(Roles.ADMIN) && !Menu.currentUser.getRoles().contains(Roles.MANAGER)) {
		    id = Menu.currentUser.getEmpId();
		    role=Roles.EMPLOYEE;
			String phnNo;
			while (true) {
				try {
					System.out.println("Enter new phnNo:");
					phnNo = sc.next();
					Util.validatePhnNo(phnNo);
					dao.updateEmployee(new Employee(id,phnNo),role);
					break;
				} catch (IllegalPhnNoException e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			role=Roles.ADMIN;
			System.out.println("Enter Id to Update:");
			id = sc.next().toUpperCase().trim();
			if (!Util.validId(id)) {
				System.out.println("please Enter valid id");
				return;
			}
			System.out.println("Enter new name to Update:");
			String newName = sc.next();
			System.out.println("Enter new dept to Update:");
			String newDept = sc.next();
			
			System.out.println("Enter new email to Update:");
			String newEmail = sc.next();
			
			System.out.println("Enter new phnNo to Update:");
			String newPhnNo = sc.next();
			dao.updateEmployee(new Employee(id,newName,newDept,newEmail,newPhnNo),role);
		}
		
		dao.fetchEmployee();
	}

}
