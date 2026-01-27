package com.employees.services;

import java.util.ArrayList;
import java.util.List;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class FetchEmployeeService {

	public List<Employee> fetchAll(EmployeeDao dao) {
		List<Employee> employees=new ArrayList<>();
		try {
			employees=dao.fetchEmployee();
		}catch (DataAccessException e) {
			throw new ServiceException("unable to fetch employee:"+e.getMessage());
		}
		return employees;
	}

	public Employee fetchById(EmployeeDao dao, String id, LoginResult currentUser) {
		Employee emp = null;
		if (currentUser.getRoles().size() == 1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {
			if (!id.equals(currentUser.getEmpId())) {
				throw new AuthorizedException("Employee can not view other employees");
			}
			try {
				emp = dao.fetchEmployeeById(currentUser.getEmpId());
			} catch (DataAccessException e) {
				throw new ServiceException("unable to fetch employee:"+e.getMessage());
			}
		}

		else {
			if (!Util.validId(id)) {
				throw new ValidationException("Invalid id");
			}
			try {
				emp = dao.fetchEmployeeById(id);
				if (emp == null) {
					throw new EmployeeNotFoundException("employee not found");
				}
			} catch (DataAccessException e) {
				throw new ServiceException("unable to fetch employee:"+e.getMessage());
			}
		}

		return emp;
	}
}
