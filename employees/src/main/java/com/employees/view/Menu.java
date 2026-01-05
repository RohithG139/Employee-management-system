package com.employees.view;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.employees.controller.EmployeeController;
import com.employees.controller.LoginController;
import com.employees.security.Operations;
import com.employees.security.RolePermission;
import com.employees.security.Roles;

public class Menu {

	public static void showMenu() throws IOException, ParseException {

		Scanner sc = new Scanner(System.in);
		EmployeeController employeeController = new EmployeeController();
		LoginController loginController=new LoginController();
		RolePermission rolePermission=new RolePermission();
		boolean loggedIn = false;
		Set<Roles> roles=new HashSet<>();
		while (!loggedIn) {
			System.out.println("Enter ID for Login");
			String id = sc.next().toUpperCase();

			System.out.println("Enter Password for Login");
			String password = sc.next();
			roles=loginController.performLogin(id, password);
			if (!roles.isEmpty()) {
				loggedIn = true;
			} else {
				System.out.println("Invalid Login,Try again.");
			}
		}
		for(Operations operation:Operations.values()) {
			if(rolePermission.hasAccess(roles,operation)) {
				System.out.println(operation);
			}
		}
		
		
		while(true) {
			System.out.println("Enter choice:");
			String input=sc.next().toUpperCase();
			Operations choice = null;
			boolean valid=false;
			try {
				choice=Operations.valueOf(input);
				valid=true;
			}catch(IllegalArgumentException e) {
				System.out.println("Invalid menu option");
			}
			
			if(valid && rolePermission.hasAccess(roles,choice)) {
				if(choice==Operations.ADD) {
					employeeController.addController();
				}
				else if(choice==Operations.UPDATE) {
					employeeController.updateController();
				}
				else if(choice==Operations.DELETE) {
					employeeController.deleteController();
				}
				else if(choice==Operations.FETCH) {
					employeeController.fetchController();
				}
				else if(choice==Operations.FETCHBYID) {
					employeeController.fetchByIdController();
				}
				else if(choice==Operations.RESETPASSWORD) {
					employeeController.resetPassword();
				}
				else if(choice==Operations.CHANGEPASSWORD) {
					employeeController.changePassword();
				}
				else if(choice==Operations.EXIT) {
					System.exit(0);
				}
				else {
					System.out.println("Please enter valid Operation");
				}
			}else {
				System.out.println("Please enter valid Operation");
			}
			
		}

	}
}
