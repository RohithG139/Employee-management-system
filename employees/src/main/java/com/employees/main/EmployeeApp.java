package com.employees.main;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.dao.FileEmployeeDaoImpl;
import com.employees.dao.JdbcEmployeeDaoImpl;
import com.employees.enums.Storage;

public class EmployeeApp {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		
		EmployeeDao dao=null;
		while (dao==null) {
			System.out.println("Select Storage?");
			for(Storage value:Storage.values()) {
				System.out.println(value);
			}
			String choice = sc.next().toUpperCase();
			try {
				Storage type = Storage.valueOf(choice);
				switch (type) {
				case FILE:
					//Menu.showMenu(new FileEmployeeDaoImpl());
					dao=new FileEmployeeDaoImpl();
					break;
				case DB:
					//Menu.showMenu(new JdbcEmployeeDaoImpl());
					dao=new JdbcEmployeeDaoImpl();
					break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("invalid storage option");
			}
			while(true) {
				Menu.showMenu(dao);
			}
		}
	}
}
