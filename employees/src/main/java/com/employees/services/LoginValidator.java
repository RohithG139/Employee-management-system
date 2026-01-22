package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class LoginValidator {
	public LoginResult login(EmployeeDao dao,String id,String password){

		
			if(!Util.validId(id)) {
				throw new ValidationException("Invalid id");
			}
			
			if(!Util.validatePassword(password)) {
				throw new ValidationException("Invalid password");
			}
			LoginResult login=dao.validateUser(id, Util.hashPassword(password));
			if(login==null) {
				throw new ValidationException("Invalid credentials,please enter valid credentials");
			}

			return login;
			
		}
	}

