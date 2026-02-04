package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;

@ExtendWith(MockitoExtension.class)
class UpdateEmployeeServiceTest {

	@Mock
	private EmployeeDao employeeDao;

	@InjectMocks
	private EmployeeService updateEmp;

	@Test
	public void empUpdate_updateOwnData_shouldCallDao() {
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.EMPLOYEE);
		LoginResult login = new LoginResult("TEK1", roles);
		Employee emp = new Employee("TEK1", "rohith@gmail.com", "8765672722");

		updateEmp.update(employeeDao, emp, login);
		verify(employeeDao).updateEmployee(emp, Roles.EMPLOYEE);
	}

	@Test
	public void empUpdate_updateOtherData_shouldThrowsException() {
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.EMPLOYEE);
		LoginResult login = new LoginResult("TEK1", roles);
		Employee emp = new Employee("TEK12", "8765432123", "rohith@gmail.com");
		AuthorizedException exception = assertThrows(AuthorizedException.class,
				() -> updateEmp.update(employeeDao, emp, login));
		assertEquals("Employees can update only their own details", exception.getMessage());
		verify(employeeDao, never()).updateEmployee(emp, Roles.EMPLOYEE);
	}

	@Test
	public void empUpdate_InvalidEmail_shouldThrowsException() {
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.EMPLOYEE);
		LoginResult login = new LoginResult("TEK1", roles);
		Employee emp = new Employee("TEK1", "8765432123", "rohithgmail.com");
		ValidationException exception = assertThrows(ValidationException.class,
				() -> updateEmp.update(employeeDao, emp, login));
		assertEquals("Invalid email", exception.getMessage());
		verify(employeeDao, never()).updateEmployee(emp, Roles.EMPLOYEE);
	}

	@Test
	void adminUpdate_validEmployee_shouldUpdateWithAdminRole() {
		Employee emp = new Employee("TEK2", "Sunny", "IT", "sunny@gmail.com", "8765432123");
		
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.ADMIN);
		LoginResult login = new LoginResult("TEK10", roles);

		updateEmp.update(employeeDao, emp, login);

		verify(employeeDao).updateEmployee(emp, Roles.ADMIN);
	}

	@Test
	void adminUpdate_validEmployee_shouldThrowsException() {
		Employee emp = new Employee("TEK2", "Sunny", "IT", "sunny@gmail.com", "8765432123");
		doThrow(new EmployeeNotFoundException("employee not found")).when(employeeDao).updateEmployee(emp,Roles.ADMIN);
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.ADMIN);
		LoginResult login = new LoginResult("TEK10", roles);
		assertThrows(EmployeeNotFoundException.class,()->updateEmp.update(employeeDao, emp, login));

		verify(employeeDao).updateEmployee(emp, Roles.ADMIN);
	}
	
}
