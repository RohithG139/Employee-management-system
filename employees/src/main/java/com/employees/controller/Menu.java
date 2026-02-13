package com.employees.controller;

import java.util.Scanner;
import java.util.Set;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Operations;
import com.employees.enums.RolePermission;
import com.employees.enums.Roles;
import com.employees.model.LoginResult;
import com.employees.model.Session;
import com.employees.utils.DatabaseConfig;

public class Menu {

	
	public static void showMenu(EmployeeDao dao) {

		Scanner sc = new Scanner(System.in);

		RolePermission rolePermission = new RolePermission();
		EmployeeController controller = new EmployeeController();
		LoginController loginController = new LoginController();

		LoginResult login = loginController.validateUser(dao);
		Session session=new Session(login);    // currently who are logged in
		Set<Roles> roles=session.getRoles();
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
				continue;
			}
			if (!rolePermission.hasAccess(roles, choice)) {
				System.out.println("Access denied...");
				continue;
			}

			switch (choice) {

			case ADD:
				controller.addEmployee(dao);
				break;

			case UPDATE:
				controller.updateEmployee(dao,session);
				break;

			case DELETE:
				controller.deleteEmployee(dao);
				break;

			case FETCH:
				controller.fetchAll(dao);
				break;

			case FETCH_EMPLOYEE_BY_ID:
				controller.fetchById(dao,session);
				break;

			case RESETPASSWORD:
				loginController.resetPassword(dao);
				break;

			case CHANGEPASSWORD:
				loginController.changePassword(dao,session);
				break;

			case ASSIGNROLE:
				controller.assignRole(dao);
				break;

			case REVOKEROLE:
				controller.revokeRole(dao);
				break;
				
			case FETCH_INACTIVE_EMPLOYEES:
				controller.fetchInActiveEmployees(dao);
				break;
				
			case LOGOUT:
				System.out.println("logout succesfully");
				return;
				
			case EXIT:
				System.out.println("EXIT...");
				DatabaseConfig.shutdown();
				sc.close();
				System.exit(0);
				
				
			default:
				System.out.println("Invalid operation");
			}

		}

	}
}
