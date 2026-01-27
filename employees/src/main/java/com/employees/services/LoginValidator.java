package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class LoginValidator {
	public LoginResult login(EmployeeDao dao,String id,String password){

		
			LoginResult login=dao.validateUser(id, Util.hashPassword(password));
			if(login==null) {
				throw new ValidationException("Incorrect username or password");
			}

			return login;
			
		}
	}

