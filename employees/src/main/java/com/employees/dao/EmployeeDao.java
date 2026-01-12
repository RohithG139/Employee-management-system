package com.employees.dao;

import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.security.Roles;

public interface EmployeeDao {
	void addEmployee(Employee e);

	void fetchEmployee();

	void fetchEmployeeById(String id);

	void deleteEmployee(String id);

	void updateEmployee(Employee emp, Roles role);

	void resetPassword(String id, String password);

	void changePassword(String id, String password);
	
	void assignRole(String id,Roles role);
	
	void revokeRole(String id,Roles role);
	
	LoginResult validateUser(String id, String password);
}
