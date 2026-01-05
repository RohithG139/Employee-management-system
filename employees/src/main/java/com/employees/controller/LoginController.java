package com.employees.controller;
import java.util.Set;

import com.employees.security.Roles;
import com.employees.security.ValidateLogin;
public class LoginController {
	public Set<Roles> performLogin(String id,String password) {
		ValidateLogin loginObj=new ValidateLogin();
		boolean validateUser=loginObj.validate(id,password);
		if(validateUser) {
			return loginObj.roles;
		}
		return null;
	}
}
