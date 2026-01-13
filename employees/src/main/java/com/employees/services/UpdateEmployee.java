package com.employees.services;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.exceptions.IllegalEmailException;
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
		    String email = "";
	        while (true) {
	            try {
	                System.out.println("Enter email:");
	                email = sc.nextLine();
	                Util.validateEmail(email);
	                break;
	            } catch (IllegalEmailException e) {
	                System.out.println("Invalid email:"+e.getMessage());
	            }
	        }

	        
	        String phnNo = "";
	        while (true) {
	            try {
	                System.out.println("Enter phnNo:");
	                phnNo = sc.nextLine();
	                Util.validatePhnNo(phnNo);
	                break;
	            } catch (IllegalPhnNoException e) {
	                System.out.println("Invalid phnNo:"+e.getMessage());
	            }
	        }
	        dao.updateEmployee(new Employee(id,phnNo,email), role);
			
		} else {
			role=Roles.ADMIN;
			System.out.println("Enter Id to Update:");
			id = sc.next().toUpperCase().trim();
			if (!Util.validId(id)) {
				System.out.println("please Enter valid id");
				return;
			}
			sc.nextLine();
			 String name = "";
		        while (true) {
		            System.out.println("Enter name:");
		            name = sc.nextLine();
		            if (Util.validateName(name)) {
		                break;
		            }
		            System.out.println("Invalid name. Name cannot be empty.");
		        }

		        String dept = "";
		        while (true) {
		            System.out.println("Enter dept:");
		            dept = sc.nextLine();
		            if (Util.validateDept(dept)) {
		                break;
		            }
		            System.out.println("Invalid department. Dept cannot be empty.");
		        }
			
		        String email = "";
		        while (true) {
		            try {
		                System.out.println("Enter email:");
		                email = sc.nextLine();
		                Util.validateEmail(email);
		                break;
		            } catch (IllegalEmailException e) {
		                System.out.println("Invalid email:"+e.getMessage());
		            }
		        }

		        
		        String phnNo = "";
		        while (true) {
		            try {
		                System.out.println("Enter phnNo:");
		                phnNo = sc.nextLine();
		                Util.validatePhnNo(phnNo);
		                break;
		            } catch (IllegalPhnNoException e) {
		                System.out.println("Invalid phnNo:"+e.getMessage());
		            }
		        }
			dao.updateEmployee(new Employee(id,name,dept,email,phnNo),role);
		}
		
		dao.fetchEmployee();
	}

}
