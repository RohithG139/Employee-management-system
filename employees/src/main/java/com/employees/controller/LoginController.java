package com.employees.controller;
import com.employees.security.ValidateLogin;
public class LoginController {
	public String performLogin(String id,String password) {
		ValidateLogin loginObj=new ValidateLogin();
		boolean validateUser=loginObj.validate(id,password);
		if(validateUser) {
			return loginObj.role;
		}
		return null;
	}
}
