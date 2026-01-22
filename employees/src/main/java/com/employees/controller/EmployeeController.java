package com.employees.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.services.AddEmployeeService;
import com.employees.services.DeleteEmployeeService;
import com.employees.services.FetchEmployeeService;
import com.employees.services.PasswordOperations;
import com.employees.services.UpdateEmployeeService;
import com.employees.services.UpdateRolesService;
import com.employees.utils.Util;

public class EmployeeController {
	private static final Scanner sc = new Scanner(System.in);

	AddEmployeeService addEmployee = new AddEmployeeService();
	UpdateEmployeeService updateEmployee = new UpdateEmployeeService();
	DeleteEmployeeService deleteEmployee = new DeleteEmployeeService();
	FetchEmployeeService fetchEmployee = new FetchEmployeeService();
	PasswordOperations passwordOperations = new PasswordOperations();
	UpdateRolesService updateRoles = new UpdateRolesService();

	public void addEmployee(EmployeeDao dao) {
		System.out.println("Enter name:");
		String name = sc.next();
		System.out.println("Enter dept:");
		String dept = sc.next();
		System.out.println("Enter email:");
		String email = sc.next();
		System.out.println("Enter phnNo:");
		String phnNo = sc.next();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();

		String password = "Tek@" + Util.generatePassword();
		String hashedPassword = Util.hashPassword(password);

		Employee employee = new Employee(name, dept, email, phnNo, null, hashedPassword);
		try {
			addEmployee.insert(dao, employee, role);
			System.out.println("Employee added succesfully");
			System.out.println("Your temporary password:" + password);
		} catch (IllegalArgumentException e) {
			System.out.println("error while insert role:" + e.getMessage());
		} catch (ValidationException e) {
			System.out.println("error while insert employee:" + e.getMessage());
		}

	}

	public void deleteEmployee(EmployeeDao dao) {
		System.out.println("Enter Id to Delete:");
		String id = sc.next().toUpperCase();
		try {
			deleteEmployee.delete(dao, id);
			System.out.println("Employee deleted succesfully");
		} catch (ValidationException e) {
			System.out.println("error while delete employee:" + e.getMessage());
		}
	}

	public void updateEmployee(EmployeeDao dao) {
		LoginResult user = Menu.currentUser;
		if (user.getRoles().size() == 1 && user.getRoles().contains(Roles.EMPLOYEE)) {
			String id = user.getEmpId();
			System.out.println("Enter email:");
			String email = sc.next();
			System.out.println("Enter phnNo:");
			String phnNo = sc.next();
			Employee employee = new Employee(id, email, phnNo);

			try {
				updateEmployee.update(dao, employee, user);
				System.out.println("Employee updated own data succesfully");
			} catch (ValidationException e) {
				System.out.println("error while updating their own details:" + e.getMessage());
			} catch (AuthorizedException e) {
				System.out.println("Authorized error:" + e.getMessage());
			}
		} else {
			System.out.println("Enter Id to update:");
			String id = sc.next().toUpperCase();
			System.out.println("Enter name:");
			String name = sc.next();
			System.out.println("Enter dept:");
			String dept = sc.next();
			System.out.println("Enter email:");
			String email = sc.next();
			System.out.println("Enter phnNo:");
			String phnNo = sc.next();

			Employee employee = new Employee(id, name, dept, email, phnNo);
			try {
				updateEmployee.update(dao, employee, user);
				System.out.println("Employee updated succesfully");
			} catch (IllegalArgumentException e) {
				System.out.println("error while updating role:" + e.getMessage());
			} catch (ValidationException e) {
				System.out.println("error while updating details:" + e.getMessage());
			}
		}
	}

	public void fetchAll(EmployeeDao dao) {
		fetchEmployee.fetchAll(dao);

	}

	public void fetchById(EmployeeDao dao) {
		System.out.println("Enter Employee id:");
		String id = sc.next().toUpperCase();
		try {
			fetchEmployee.fetchById(dao, id, Menu.currentUser);
		} catch (ValidationException e) {
			System.out.println("error while fetching details:" + e.getMessage());
		} catch (AuthorizedException e) {
			System.out.println("Authorized error:" + e.getMessage());
		}
	}

	public void assignRole(EmployeeDao dao) {
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();
		try {
			updateRoles.assignRole(dao, id, role);
		} catch (ValidationException e) {
			System.out.println("error while assign role:" + e.getMessage());
		}
	}

	public void revokeRole(EmployeeDao dao) {
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		System.out.println("Enter role:");
		String role = sc.next().toUpperCase();
		try {
			updateRoles.revokeRole(dao, id, role);
		} catch (ValidationException e) {
			System.out.println("error while revoke role:" + e.getMessage());
		}
	}
}
