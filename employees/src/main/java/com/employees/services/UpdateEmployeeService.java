package com.employees.services;

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

public class UpdateEmployeeService {

	public void update(EmployeeDao dao, Employee employee, LoginResult currentUser) {
		if (currentUser.getRoles().size() == 1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {

			if (!employee.getId().equals(currentUser.getEmpId())) {
				throw new AuthorizedException("Employees can update only their own details");
			}
			if (!Util.validateEmail(employee.getEmail())) {
				throw new ValidationException("Invalid email");
			}
			if (!Util.validatePhnNo(employee.getPhnNo())) {
				throw new ValidationException("Invalid phnNo");
			}

			try {
				dao.updateEmployee(employee, Roles.EMPLOYEE);

			} catch (DataAccessException e) {
				throw new ServiceException("unable to update employee:" + e.getMessage());
			}
		} else {
			if (!Util.validId(employee.getId())) {
				throw new ValidationException("Invalid id");
			}

			if (!Util.validateName(employee.getName())) {
				throw new ValidationException("Invalid name");
			}
			if (!Util.validateDept(employee.getDept())) {
				throw new ValidationException("Invalid department");
			}
			if (!Util.validateEmail(employee.getEmail())) {
				throw new ValidationException("Invalid email");
			}
			if (!Util.validatePhnNo(employee.getPhnNo())) {
				throw new ValidationException("Invalid phnNo");
			}
			try {
				boolean result = dao.updateEmployee(employee, Roles.ADMIN);
				if (!result) {
					throw new EmployeeNotFoundException("employee not found");
				}

			} catch (DataAccessException e) {
				throw new ServiceException("unable to update employee:" + e.getMessage());
			}

		}
	}
}
