package com.employees.services;

import java.util.Scanner;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.utils.Util;

public class UpdateRoles {
	public void assignRole(EmployeeDao dao) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		if (!Util.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		System.out.println("Enter role:");
		String role=sc.next().toUpperCase();
		
		dao.assignRole(id,Roles.valueOf(role));
	}

	public void revokeRole(EmployeeDao dao) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Id:");
		String id = sc.next().toUpperCase();
		if (!Util.validId(id)) {
			System.out.println("please Enter valid id");
			return;
		}
		System.out.println("Enter role:");
		String role=sc.next().toUpperCase();
		
		dao.revokeRole(id,Roles.valueOf(role));
	}
}
