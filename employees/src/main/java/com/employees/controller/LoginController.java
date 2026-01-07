package com.employees.controller;


import com.employees.services.LoginValidator;

public class LoginController {
	public boolean performLogin() {
		LoginValidator loginObj=new LoginValidator();
		boolean validateUser=loginObj.validate();
		if(validateUser) {
			return true;
		}
		return false;
	}
}
