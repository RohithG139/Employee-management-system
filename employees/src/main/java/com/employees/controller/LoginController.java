package com.employees.controller;


import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.employees.services.LoginValidator;

public class LoginController {
	public boolean performLogin() throws ParseException, IOException {
		LoginValidator loginObj=new LoginValidator();
		boolean validateUser=loginObj.validate();
		if(validateUser) {
			return true;
		}
		return false;
	}
}
