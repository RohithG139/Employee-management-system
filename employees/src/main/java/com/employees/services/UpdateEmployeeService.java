package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class UpdateEmployeeService {
	
	//public void update(EmployeeDao dao,String id,LoginResult currentUser) {
//		if (!Menu.currentUser.getRoles().contains(Roles.ADMIN) && !Menu.currentUser.getRoles().contains(Roles.MANAGER)) {
//		    id = Menu.currentUser.getEmpId();
//		    role=Roles.EMPLOYEE;
//		    String email = "";
//	        while (true) {
//	            try {
//	                System.out.println("Enter email:");
//	                email = sc.nextLine();
//	                Util.validateEmail(email);
//	                break;
//	            } catch (IllegalEmailException e) {
//	                System.out.println("Invalid email:"+e.getMessage());
//	            }
//	        }
//
//	        
//	        String phnNo = "";
//	        while (true) {
//	            try {
//	                System.out.println("Enter phnNo:");
//	                phnNo = sc.nextLine();
//	                Util.validatePhnNo(phnNo);
//	                break;
//	            } catch (IllegalPhnNoException e) {
//	                System.out.println("Invalid phnNo:"+e.getMessage());
//	            }
//	        }
//	        dao.updateEmployee(new Employee(id,phnNo,email), role);
//			
//		} else {
//			role=Roles.ADMIN;
//			System.out.println("Enter Id to Update:");
//			id = sc.next().toUpperCase().trim();
//			if (!Util.validId(id)) {
//				System.out.println("please Enter valid id");
//				return;
//			}
//			sc.nextLine();
//			 String name = "";
//		        while (true) {
//		            System.out.println("Enter name:");
//		            name = sc.nextLine();
//		            if (Util.validateName(name)) {
//		                break;
//		            }
//		            System.out.println("Invalid name. Name cannot be empty.");
//		        }
//
//		        String dept = "";
//		        while (true) {
//		            System.out.println("Enter dept:");
//		            dept = sc.nextLine();
//		            if (Util.validateDept(dept)) {
//		                break;
//		            }
//		            System.out.println("Invalid department. Dept cannot be empty.");
//		        }
//			
//		        String email = "";
//		        while (true) {
//		            try {
//		                System.out.println("Enter email:");
//		                email = sc.nextLine();
//		                Util.validateEmail(email);
//		                break;
//		            } catch (IllegalEmailException e) {
//		                System.out.println("Invalid email:"+e.getMessage());
//		            }
//		        }
//
//		        
//		        String phnNo = "";
//		        while (true) {
//		            try {
//		                System.out.println("Enter phnNo:");
//		                phnNo = sc.nextLine();
//		                Util.validatePhnNo(phnNo);
//		                break;
//		            } catch (IllegalPhnNoException e) {
//		                System.out.println("Invalid phnNo:"+e.getMessage());
//		            }
//		        }
//			dao.updateEmployee(new Employee(id,name,dept,email,phnNo),role);
//		}
//		
//		dao.fetchEmployee();
//	}
//}
	
	public void update(EmployeeDao dao,Employee employee,LoginResult currentUser) {
		if (currentUser.getRoles().size()==1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {
			
			if(!employee.getId().equals(currentUser.getEmpId()))
			{
				throw new AuthorizedException("Employees can update only their own details");
			}
			if(!Util.validateEmail(employee.getEmail())) {
				throw new ValidationException("Invalid email");
			}
			if(!Util.validatePhnNo(employee.getPhnNo())) {
				throw new ValidationException("Invalid phnNo");
			}
			
			dao.updateEmployee(employee, Roles.EMPLOYEE);
		}
		else {
			if(!Util.validId(employee.getId())) {
				throw new ValidationException("Invalid id");
			}
				
			if (!Util.validateName(employee.getName())) {
	            throw new ValidationException("Invalid name");
			}
	        if (!Util.validateDept(employee.getDept())) {
	            throw new ValidationException("Invalid department");
	        }
	        if (!Util.validateEmail(employee.getEmail())) {
	            throw new ValidationException("Invalid email");
	        }
	        if (!Util.validatePhnNo(employee.getPhnNo())) {
	            throw new ValidationException("Invalid phnNo");
	        }
	        dao.updateEmployee(employee, Roles.ADMIN);
		}
	}
}
