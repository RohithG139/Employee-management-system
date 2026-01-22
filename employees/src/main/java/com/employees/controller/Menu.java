package com.employees.controller;

import java.util.Scanner;
import java.util.Set;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Operations;
import com.employees.enums.RolePermission;
import com.employees.enums.Roles;
import com.employees.model.LoginResult;
import com.employees.services.AddEmployeeService;
import com.employees.services.DeleteEmployeeService;
import com.employees.services.FetchEmployeeService;
import com.employees.services.LoginValidator;
import com.employees.services.PasswordOperations;
import com.employees.services.UpdateEmployeeService;
import com.employees.services.UpdateRolesService;

public class Menu {

	public static LoginResult currentUser;

	public static void showMenu(EmployeeDao dao) {

		Scanner sc = new Scanner(System.in);

		RolePermission rolePermission = new RolePermission();
		EmployeeController controller = new EmployeeController();
		LoginController loginController = new LoginController();

		LoginResult login = loginController.validateUser(dao);
		if (login == null)
			return;
		Set<Roles> roles = login.getRoles();
		Menu.currentUser = login; // currently who are logged in

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
			if (!rolePermission.hasAccess(roles, choice)) {
				System.out.println("Access denied...");
			}

			if (rolePermission.hasAccess(roles, choice)) {
				if (choice == Operations.ADD) {

					controller.addEmployee(dao);
				}
				if (choice == Operations.UPDATE) {

					controller.updateEmployee(dao);
				}
				if (choice == Operations.DELETE) {
					controller.deleteEmployee(dao);

				}
				if (choice == Operations.FETCH) {

					controller.fetchAll(dao);
				}
				if (choice == Operations.FETCH_EMPLOYEE_BY_ID) {

					controller.fetchById(dao);
				}
				if (choice == Operations.RESETPASSWORD) {

					loginController.resetPassword(dao);
				}
				if (choice == Operations.CHANGEPASSWORD) {

					loginController.changePassword(dao);
				}
				if (choice == Operations.ASSIGNROLE) {

					controller.assignRole(dao);
				}
				if (choice == Operations.REVOKEROLE) {

					controller.revokeRole(dao);
				}
				if (choice == Operations.EXIT) {
					System.out.println("EXIT...");
					System.exit(0);
				}
			}

		}

	}
}
