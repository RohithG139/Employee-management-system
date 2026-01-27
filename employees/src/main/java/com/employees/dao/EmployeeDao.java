package com.employees.dao;

import java.util.List;

import com.employees.enums.Roles;
import com.employees.model.Employee;
import com.employees.model.LoginResult;

public interface EmployeeDao {
	void addEmployee(Employee e);

	List<Employee> fetchEmployee();

	Employee fetchEmployeeById(String id);

	boolean deleteEmployee(String id);

	boolean updateEmployee(Employee emp, Roles role);

	boolean resetPassword(String id, String password);

	boolean changePassword(String id, String password);
	
	boolean assignRole(String id,Roles role);
	
	boolean revokeRole(String id,Roles role);
	
	LoginResult validateUser(String id, String password);
}
