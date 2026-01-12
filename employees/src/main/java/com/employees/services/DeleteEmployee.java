package com.employees.services;

import java.util.Scanner;

import com.employees.dao.EmployeeDao;
import com.employees.utils.Util;

public class DeleteEmployee {
	public void delete(EmployeeDao dao) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Id to Delete:");
		String id = sc.next().toUpperCase();
		if (!Util.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}

		dao.deleteEmployee(id);
		dao.fetchEmployee();
	}
}
