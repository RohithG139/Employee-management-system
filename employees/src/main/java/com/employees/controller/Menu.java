package com.employees.controller;

import java.util.Scanner;
import java.util.Set;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Operations;
import com.employees.enums.RolePermission;
import com.employees.enums.Roles;
import com.employees.model.LoginResult;
import com.employees.services.AddEmployee;
import com.employees.services.DeleteEmployee;
import com.employees.services.FetchEmployee;
import com.employees.services.LoginValidator;
import com.employees.services.PasswordOperations;
import com.employees.services.UpdateEmployee;
import com.employees.services.UpdateRoles;

public class Menu {

	public static LoginResult currentUser;
	public static void showMenu(EmployeeDao dao) {

		Scanner sc = new Scanner(System.in);
		AddEmployee addEmployee = new AddEmployee();
		UpdateEmployee updateEmployee = new UpdateEmployee();
		DeleteEmployee deleteEmployee = new DeleteEmployee();
		FetchEmployee fetchEmployee = new FetchEmployee();
		PasswordOperations passwordOperations = new PasswordOperations();
		UpdateRoles updateRoles=new UpdateRoles();
		LoginValidator validator=new LoginValidator();
		RolePermission rolePermission = new RolePermission();

		LoginResult login = validator.validate(dao);
		if (login == null)
			return;
		Set<Roles> roles = login.getRoles();
		Menu.currentUser = login;  //currently who are logged in
		
		while (true) {
			for (Operations operation : Operations.values()) {
				if (rolePermission.hasAccess(roles, operation)) {
					System.out.println(operation);
				}
			}
			System.out.println("Enter choice:");
			String input = sc.next().toUpperCase();
			Operations choice = null;
			try {
				choice = Operations.valueOf(input);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid menu option");
			}

			if (rolePermission.hasAccess(roles, choice)) {
				if (choice == Operations.ADD) {
					addEmployee.insert(dao);
				}
				if (choice == Operations.UPDATE) {
					updateEmployee.update(dao);
				}
				if (choice == Operations.DELETE) {
					deleteEmployee.delete(dao);
				}
				if (choice == Operations.FETCH) {
					fetchEmployee.fetchAll(dao);
				}
				if (choice == Operations.FETCH_EMPLOYEE_BY_ID) {
					fetchEmployee.fetchById(dao);
				}
				if (choice == Operations.RESETPASSWORD) {
					passwordOperations.resetPassword(dao);
				}
				if (choice == Operations.CHANGEPASSWORD) {
					passwordOperations.changePassword(dao);
				}
				if (choice == Operations.ASSIGNROLE) {
					updateRoles.assignRole(dao);
				}
				if (choice == Operations.REVOKEROLE) {
					updateRoles.revokeRole(dao);
				}
				if (choice == Operations.EXIT) {
					System.out.println("EXIT...");
					System.exit(0);
				}
			}

		}

	}
}
