package com.employees.controller;

import java.util.List;
import java.util.Scanner;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.model.Session;
import com.employees.services.EmployeeService;
import com.employees.utils.Util;

public class EmployeeController {
	private static final Scanner sc = new Scanner(System.in);
	EmployeeService employeeService=new EmployeeService();

	public void addEmployee(EmployeeDao dao) {
		sc.nextLine();
		System.out.println("Enter name:");
		String name = sc.nextLine();
		System.out.println("Enter dept:");
		String dept = sc.nextLine();
		System.out.println("Enter email:");
		String email = sc.nextLine();
		System.out.println("Enter phnNo:");
		String phnNo = sc.nextLine();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();

		String password = "Tek@" + Util.generatePassword();
		String hashedPassword = Util.hashPassword(password);

		Employee employee = new Employee(name, dept, email, phnNo, null, hashedPassword);
		try {
			employeeService.insert(dao, employee, role);
			System.out.println("Employee added succesfully");
			System.out.println("Your temporary password:" + password);
		} catch (ValidationException e) {
			System.out.println("error while insert employee:" + e.getMessage());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteEmployee(EmployeeDao dao) {
		System.out.println("Enter Id to Delete:");
		String id = sc.next().toUpperCase();
		try {
			employeeService.delete(dao, id);
			System.out.println("Employee deleted succesfully");
		} catch (ValidationException e) {
			System.out.println("error while delete employee:" + e.getMessage());
		} catch (EmployeeNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateEmployee(EmployeeDao dao,Session session) {
		LoginResult user = session.getUser();
		if (user.getRoles().size() == 1 && user.getRoles().contains(Roles.EMPLOYEE)) {
			String id = user.getEmpId();
			System.out.println("Enter email:");
			String email = sc.nextLine();
			System.out.println("Enter phnNo:");
			String phnNo = sc.nextLine();
			Employee employee = new Employee(id, email, phnNo);

			try {
				employeeService.update(dao, employee, user);
				System.out.println("Employee updated their own data succesfully");
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
			} catch (AuthorizedException e) {
				System.out.println("Authorized error:" + e.getMessage());
			}  catch (ServiceException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Enter Id to update:");
			String id = sc.next().toUpperCase();
			sc.nextLine();
			System.out.println("Enter name:");
			String name = sc.nextLine();
			System.out.println("Enter dept:");
			String dept = sc.nextLine();
			System.out.println("Enter email:");
			String email = sc.nextLine();
			System.out.println("Enter phnNo:");
			String phnNo = sc.nextLine();

			Employee employee = new Employee(id, name, dept, email, phnNo);
			try {
				employeeService.update(dao, employee, user);
				System.out.println("Employee updated succesfully");
			} catch (ValidationException e) {
				System.out.println("error while updating details:" + e.getMessage());
			} catch (EmployeeNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (ServiceException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void fetchAll(EmployeeDao dao) {
		try {
			List<Employee> employees = employeeService.fetchAll(dao);
			for (Employee employee : employees) {
				System.out.println(employee.toString());
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}

	}

	public void fetchById(EmployeeDao dao,Session session) {
		
		LoginResult user = session.getUser();
		if (user.getRoles().size() == 1 && user.getRoles().contains(Roles.EMPLOYEE)) {
			try {
				Employee employee = employeeService.fetchById(dao,user.getEmpId(), user);
				System.out.println(employee.toString());
			} catch (ValidationException e) {
				System.out.println("error while fetching details:" + e.getMessage());
			} catch (AuthorizedException e) {
				System.out.println("Authorized error:" + e.getMessage());
			} catch (ServiceException e) {
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("Enter Employee id:");
			String id = sc.next().toUpperCase();
			try {
				Employee employee = employeeService.fetchById(dao, id, user);
				System.out.println(employee.toString());
			} catch (ValidationException e) {
				System.out.println("error while fetching details:" + e.getMessage());
			} catch (AuthorizedException e) {
				System.out.println("Authorized error:" + e.getMessage());
			} catch (ServiceException e) {
				System.out.println(e.getMessage());
			}catch (EmployeeNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void assignRole(EmployeeDao dao) {
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();
		try {
			employeeService.assignRole(dao, id, role);
			System.out.println("role assigned succesfully");
		} catch (ValidationException e) {
			System.out.println("error while assign role:" + e.getMessage());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}catch (EmployeeNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void revokeRole(EmployeeDao dao) {
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();
		try {
			employeeService.revokeRole(dao, id, role);
			System.out.println("role revoked succesfully");
		} catch (ValidationException e) {
			System.out.println("error while revoke role:" + e.getMessage());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}catch (EmployeeNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void fetchInActiveEmployees(EmployeeDao dao) {
		try {
			List<Employee> employees = employeeService.fetchInActiveEmployees(dao);
			for (Employee employee : employees) {
				System.out.println(employee.toString());
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}
}
