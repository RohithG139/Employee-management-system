package com.employees.view;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.employees.controller.EmployeeController;
import com.employees.controller.LoginController;
import com.employees.dao.EmployeeDaoImpl;
import com.employees.security.Operations;
import com.employees.security.RolePermission;
import com.employees.security.Roles;
import com.employees.services.LoginValidator;


public class Menu {

	public static void showMenu() throws IOException, ParseException {

		Scanner sc = new Scanner(System.in);
		EmployeeController employeeController = new EmployeeController();
		LoginController loginController=new LoginController();
		RolePermission rolePermission=new RolePermission();
		Set<Roles> roles=new HashSet<>();
	
		if(loginController.performLogin()) {
			roles=EmployeeDaoImpl.roles;
		
		while(true) {
			for(Operations operation:Operations.values()) {
				if(rolePermission.hasAccess(roles,operation)) {
					System.out.println(operation);
				}
			}
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
			    if(choice==Operations.UPDATE && (roles.contains(Roles.ADMIN) || roles.contains(Roles.MANAGER))) {
					employeeController.updateController();
				}
				if(choice==Operations.UPDATE && roles.contains(Roles.EMPLOYEE)) {
					employeeController.updateUserController();
				}
				if(choice==Operations.DELETE) {
					employeeController.deleteController();
				}
				if(choice==Operations.FETCH) {
					employeeController.fetchController();
				}
				if(choice==Operations.FETCH_EMPLOYEE_BY_ID) {
					employeeController.fetchByIdController();
				}
				 if(choice==Operations.RESETPASSWORD) {
					employeeController.resetPassword();
				}
				 if(choice==Operations.CHANGEPASSWORD) {
					employeeController.changePassword();
				}
				 if(choice==Operations.EXIT) {
					System.exit(0);
				}
			}else {
				System.out.println("You dont have a permission:");
			}
			
		}
		}

	}
}
