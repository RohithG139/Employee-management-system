package com.employees.dao;

import java.util.Map;

import com.employees.model.Employee;
import com.employees.model.LoginResult;

public interface EmployeeDao {
	void addEmployee(Employee e);

	void fetchEmployee();

	void fetchEmployeeById(String id);

	void deleteEmployee(String id);

	void updateEmployee(String id, Map<String, String> map);

	void resetPassword(String id, String password);

	void changePassword(String id, String password);
	
	LoginResult validateUser(String id,String password);
}
