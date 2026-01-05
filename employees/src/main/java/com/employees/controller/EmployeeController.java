package com.employees.controller;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;
import com.employees.services.AddEmployee;
import com.employees.services.DeleteEmployee;
import com.employees.services.FetchEmployee;
import com.employees.services.PasswordOperations;
import com.employees.services.UpdateEmployee;

public class EmployeeController {
	public void addController() throws IOException,ParseException{
		AddEmployee addObj=new AddEmployee();
		addObj.insert();
	}
	public void updateController() throws ParseException,IOException {
		UpdateEmployee updateObj=new UpdateEmployee();
		updateObj.update();
	}
	
	public void deleteController() throws ParseException,IOException{
		DeleteEmployee deleteObj=new DeleteEmployee();
		deleteObj.delete();
	}
	
	public void fetchController() throws ParseException,IOException{
		FetchEmployee fetchObj=new FetchEmployee();
		fetchObj.fetchAll();
	}
	
	public void fetchByIdController() throws ParseException,IOException{
		FetchEmployee fetchObj=new FetchEmployee();
		fetchObj.fetchById();
	}
	public void resetPassword() throws ParseException, IOException{
		PasswordOperations passObj=new PasswordOperations();
		passObj.resetPassword();
	}
	public void changePassword() throws ParseException, IOException{
		PasswordOperations passObj=new PasswordOperations();
		passObj.changePassword();
	}
}
