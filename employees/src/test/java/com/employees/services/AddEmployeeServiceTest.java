package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;

@ExtendWith(MockitoExtension.class)
class AddEmployeeServiceTest {
	
	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private AddEmployeeService add;
	
	@Test
	public void addEmployee_validEmployee_shouldReturnDao() {
			Employee emp=new Employee(
					"Rohith","dev","rohith@gmail.com","8788222382",null,"hashedPWD");
			
			add.insert(employeeDao, emp,"ADMIN");
			
			verify(employeeDao).addEmployee(emp);
	}
	
	
	@Test
	public void addEmployee_InvalidEmail_shouldThrowsException() {
			Employee emp=new Employee(
					"Rohith","dev","rohithgmail.com","8788222382",null,"hashedPWD");
			
			ValidationException exception=assertThrows(ValidationException.class,()->add.insert(employeeDao, emp,"ADMIN"));
			
			assertEquals("Invalid email",exception.getMessage());
	}
	
	@Test
	public void addEmployee_InvalidRole_shouldThrowsException() {
			Employee emp=new Employee(
					"Rohith","dev","rohith@gmail.com","8788222382",null,"hashedPWD");
			
			IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->add.insert(employeeDao, emp,"ADMIN1"));
			
			assertEquals("Invalid role",exception.getMessage());
	}
	
	
	
}
