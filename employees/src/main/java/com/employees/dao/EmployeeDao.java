package com.employees.dao;

import java.util.List;

import com.employees.enums.Roles;
import com.employees.model.Employee;
import com.employees.model.LoginResult;

public interface EmployeeDao {
	void addEmployee(Employee e);

	List<Employee> fetchEmployee();

	Employee fetchEmployeeById(String id);

	void deleteEmployee(String id);

	void updateEmployee(Employee emp, Roles role);

	void resetPassword(String id, String password);

	void changePassword(String id, String password);
	
	void assignRole(String id,Roles role);
	
	void revokeRole(String id,Roles role);
	
	List<Employee> fetchInActiveEmployees();
	
	LoginResult validateUser(String id, String password);
}
