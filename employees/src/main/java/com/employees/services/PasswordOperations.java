package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
import com.employees.utils.Util;

public class PasswordOperations {

	public String resetPasswordService(EmployeeDao dao, String id) {

		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		String password = "Tek@" + Util.generatePassword();

		boolean result = dao.resetPassword(id, Util.hashPassword(password));
		if (!result) {
			throw new EmployeeNotFoundException("employee not found");
		}

		return password;
	}

	public void changePasswordService(EmployeeDao dao, String id, String password) {

		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		if (!Util.validatePassword(password)) {
			throw new ValidationException("Invalid password");
		}
		boolean result = dao.changePassword(id, Util.hashPassword(password));
		if (!result) {
			throw new EmployeeNotFoundException("employee not found");
		}

	}
}
