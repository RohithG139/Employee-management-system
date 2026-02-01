package com.employees.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	public LoginResult login(EmployeeDao dao, String id, String password) {
		logger.info("login request recieved for id {} ", id);
		LoginResult login = dao.validateUser(id, Util.hashPassword(password));
		if (login == null) {
			logger.warn("Login failure for id {}", id);
			throw new ValidationException("Incorrect username or password");
		}

		return login;

	}

	public String resetPasswordService(EmployeeDao dao, String id) {
		logger.info("reset password request recieved for id {} ", id);
		if (!Util.validId(id)) {
			logger.warn("validation failed: invalid id for {}", id);
			throw new ValidationException("Invalid id");
		}
		String password = "Tek@" + Util.generatePassword();
		try {
			boolean result = dao.resetPassword(id, Util.hashPassword(password));
			if (!result) {
				logger.warn("employee not found with id {} ", id);
				throw new EmployeeNotFoundException("employee not found");
			}
			logger.info("reset password successfully for id {} ", id);
		} catch (DataAccessException e) {
			logger.error("Database error while reset the password for id {} ", id, e);
			throw new ServiceException("unable to change password" + e.getMessage());
		}

		return password;
	}

	public void changePasswordService(EmployeeDao dao, String id, String password) {
		logger.info("change password request recieved for id {} ", id);
		if (!Util.validId(id)) {
			logger.warn("validation failed: invalid id for id {}", id);
			throw new ValidationException("Invalid id");
		}
		if (!Util.validatePassword(password)) {
			logger.warn("validation failed: invalid password for id {}", id);
			throw new ValidationException("Invalid password");
		}
		try {
			boolean result = dao.changePassword(id, Util.hashPassword(password));
			if (!result) {
				logger.warn("employee not found with id {} ", id);
				throw new EmployeeNotFoundException("employee not found");
			}
			logger.info("change password successfully for id {} ", id);
		} catch (DataAccessException e) {
			logger.error("Database error while change the password for id {} ", id, e);
			throw new ServiceException("unable to change password" + e.getMessage());
		}

	}
}
