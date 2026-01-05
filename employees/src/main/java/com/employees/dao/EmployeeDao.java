package com.employees.dao;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.employees.model.Employee;

public interface EmployeeDao {
	void addEmployee(Employee e) throws ParseException,IOException;
	void fetchEmployee() throws ParseException,IOException;
	void fetchEmployeeById(String id) throws ParseException,IOException;
	void deleteEmployee(String id) throws ParseException,IOException;
	void updateEmployee(String id,String name) throws ParseException,IOException;
	void resetPassword(String id) throws ParseException, IOException;
	void changePassword(String id,String password) throws ParseException, IOException;
	
}
