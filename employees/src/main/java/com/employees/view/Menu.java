package com.employees.view;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import com.employees.controller.EmployeeController;
import com.employees.controller.LoginController;
import com.employees.security.Operations;
import com.employees.security.RolePermission;

public class Menu {

	public static void showMenu() throws IOException, ParseException {

		Scanner sc = new Scanner(System.in);
		EmployeeController controller = new EmployeeController();
		LoginController loginController=new LoginController();
		RolePermission rolePermission=new RolePermission();
		boolean loggedIn = false;
		String role=null;
		while (!loggedIn) {
			System.out.println("Enter ID for Login");
			String id = sc.next();

			System.out.println("Enter Password for Login");
			String password = sc.next();
			role=loginController.performLogin(id, password);
			if (role!=null) {
				loggedIn = true;
			} else {
				System.out.println("Invalid Login,Try again.");
			}
		}
		
		System.out.println("Welcome to "+role+" Management System");
		for(Operations operation:Operations.values()) {
			if(rolePermission.hasAccess(role,operation)) {
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
			
			if(valid && rolePermission.hasAccess(role,choice)) {
				if(choice==Operations.ADD) {
					controller.addController();
				}
				else if(choice==Operations.UPDATE) {
					controller.updateController();
				}
				else if(choice==Operations.DELETE) {
					controller.deleteController();
				}
				else if(choice==Operations.FETCH) {
					controller.fetchController();
				}
				else if(choice==Operations.FETCHBYID) {
					controller.fetchByIdController();
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
		
		
//		while (true) {
//			System.out.println("--------------");
//			for (Operations op : Operations.values()) {
//				System.out.println(op);
//			}
//
//			System.out.print("Enter choice: ");
//			String str = sc.next().trim().toUpperCase();
//
//			Operations choice;
//			try {
//				choice = Operations.valueOf(str);
//			} catch (IllegalArgumentException e) {
//				System.out.println("Invalid menu option");
//				continue;
//			}
//
//			try {
//				switch (choice) {
//				case ADD:
//					controller.addController();
//					break;
//
//				case UPDATE:
//					controller.updateController();
//					break;
//
//				case FETCH:
//					controller.fetchController();
//					break;
//
//				case DELETE:
//					controller.deleteController();
//					break;
//
//				case FETCHBYID:
//					controller.fetchByIdController();
//					break;
//
//				case EXIT:
//					System.exit(0);
//				}
//			} catch (IllegalArgumentException e) {
//				System.out.println(e.getMessage());
//			}
//		}

	}
}
