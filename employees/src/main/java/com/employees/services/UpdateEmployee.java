package com.employees.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.employees.controller.Menu;
import com.employees.dao.EmployeeDao;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.security.Roles;
import com.employees.utils.Util;

public class UpdateEmployee {
	Scanner sc = new Scanner(System.in);
	LoginValidator loginObj = new LoginValidator();

	public void update(EmployeeDao dao) {
		String id;
		Map<String, String> map = new HashMap<>();
		if (Menu.currentUser.getRoles().contains(Roles.EMPLOYEE)) {
		    id = Menu.currentUser.getEmpId();

			String phnNo;
			while (true) {
				try {
					System.out.println("Enter new phnNo:");
					phnNo = sc.next();
					Util.validatePhnNo(phnNo);
					map.put("phnNo", phnNo);
					break;
				} catch (IllegalPhnNoException e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			System.out.println("Enter Id to Update:");
			id = sc.next().toUpperCase();
			if (!Util.validId(id)) {
				System.out.println("please Enter valid id");
				return;
			}
			System.out.println("Enter new name to Update:");
			String newName = sc.next();
			map.put("name", newName);
			System.out.println("Enter new dept to Update:");
			String newDept = sc.next();
			map.put("dept", newDept);
			System.out.println("Enter new email to Update:");
			String newEmail = sc.next();
			map.put("email", newEmail);
			System.out.println("Enter new phnNo to Update:");
			String newPhnNo = sc.next();
			map.put("phnNo", newPhnNo);
		}
		dao.updateEmployee(id, map);
		System.out.println("Employee updated Successfully");
	}

}
