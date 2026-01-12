package com.employees.services;

import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.security.Roles;
import com.employees.utils.Util;

public class FetchEmployee {
	public void fetchAll(EmployeeDao dao) {
		dao.fetchEmployee();
	}

	public void fetchById(EmployeeDao dao) {
		Scanner sc = new Scanner(System.in);
		String id;
		if (Menu.currentUser.getRoles().contains(Roles.EMPLOYEE)) {
		    id = Menu.currentUser.getEmpId();
		}

		else {
			System.out.println("Enter Employee id:");
			id = sc.next().toUpperCase();
			if (!Util.validId(id)) {
				System.out.println("please Enter valid id");
				return;
			}
		}
		dao.fetchEmployeeById(id);

	}
}
