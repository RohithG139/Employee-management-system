package com.employees.services;

import java.util.HashSet;
import java.util.Set;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.utils.Util;

public class AddEmployeeService {

	public void insert(EmployeeDao dao, Employee employee, String role) {
		if (!Util.validateName(employee.getName()))
			throw new ValidationException("Invalid name");

		if (!Util.validateDept(employee.getDept()))
			throw new ValidationException("Invalid department");

		if (!Util.validateEmail(employee.getEmail()))
			throw new ValidationException("Invalid email");

		if (!Util.validatePhnNo(employee.getPhnNo()))
			throw new ValidationException("Invalid phone number");

		if (!Util.validateRole(role)) {
			throw new ValidationException("Invalid role");
		} else {
			Set<Roles> roleList = new HashSet<>();
			roleList.add(Roles.valueOf(role));
			employee.setRole(roleList);
		}
		try {
			dao.addEmployee(employee);
		}catch(DataAccessException e) {
			throw new ServiceException("unable to add employee:"+e.getMessage());
		}

	}
}
