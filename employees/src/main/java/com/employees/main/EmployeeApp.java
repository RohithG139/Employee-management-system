package com.employees.main;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.FileEmployeeDaoImpl;
import com.employees.dao.JdbcEmployeeDaoImpl;
import com.employees.storageType.Storage;

public class EmployeeApp {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Select Storage?");
		
		
		while (true) {
			for(Storage value:Storage.values()) {
				System.out.println(value);
			}
			String choice = sc.next().toUpperCase();
			try {
				Storage type = Storage.valueOf(choice);
				switch (type) {
				case FILE:
					Menu.showMenu(new FileEmployeeDaoImpl());
					break;
				case POSTGRESQL:
					Menu.showMenu(new JdbcEmployeeDaoImpl());
					break;

				}
			} catch (

			IllegalArgumentException e) {
				System.out.println("invalid storage option");
			}

		}
	}
}
