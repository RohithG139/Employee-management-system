package com.employees.controller;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import com.employees.services.AddEmployee;
import com.employees.services.DeleteEmployee;
import com.employees.services.FetchEmployee;
import com.employees.services.PasswordOperations;
import com.employees.services.UpdateEmployee;

public class EmployeeController {
	AddEmployee addObj=new AddEmployee();
	UpdateEmployee updateObj=new UpdateEmployee();
	DeleteEmployee deleteObj=new DeleteEmployee();
	FetchEmployee fetchObj=new FetchEmployee();
	PasswordOperations passObj=new PasswordOperations();
	public void addController() throws IOException,ParseException{
		
		addObj.insert();
	}
	public void updateController() throws ParseException,IOException {
		
		updateObj.update();
	}
	
	public void updateUserController() throws ParseException,IOException {
		updateObj.updateUserLogin();
	}
	public void deleteController() throws ParseException,IOException{
	
		deleteObj.delete();
	}
	
	public void fetchController() throws ParseException,IOException{
		
		fetchObj.fetchAll();
	}
	
	public void fetchByIdController() throws ParseException,IOException{
		fetchObj.fetchById();
	}
	public void resetPassword() throws ParseException, IOException{
		
		passObj.resetPassword();
	}
	public void changePassword() throws ParseException, IOException{
		passObj.changePassword();
	}
	
}
