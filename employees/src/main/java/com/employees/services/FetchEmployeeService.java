package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class FetchEmployeeService {
	
	
	public void fetchAll(EmployeeDao dao) {
		dao.fetchEmployee();
	}

	public void fetchById(EmployeeDao dao, String id, LoginResult currentUser) {
		
		if (currentUser.getRoles().size()==1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {
			if (!id.equals(currentUser.getEmpId())) {
				throw new AuthorizedException("Employee can not view other employees");
			}
			dao.fetchEmployeeById(currentUser.getEmpId());
		}
		
		else {
			if (!Util.validId(id)) {
				throw new ValidationException("Invalid id");
			}
			dao.fetchEmployeeById(id);
		}
		

	}
}
