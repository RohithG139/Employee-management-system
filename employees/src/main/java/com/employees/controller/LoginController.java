package com.employees.controller;
import java.util.Set;

import com.employees.security.Roles;
import com.employees.security.ValidateLogin;
public class LoginController {
	public boolean performLogin(String userName,String password) {
		ValidateLogin loginObj=new ValidateLogin();
		boolean validateUser=loginObj.validate(userName,password);
		if(validateUser) {
			return true;
		}
		return false;
	}
}
