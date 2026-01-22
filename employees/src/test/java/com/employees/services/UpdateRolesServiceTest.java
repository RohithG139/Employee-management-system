package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class UpdateRolesServiceTest {

	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private UpdateRolesService updateRole;
	
	@Test
	public void updateRoles_validId_doesNotThrowsException() {
		
		assertDoesNotThrow(()->updateRole.assignRole(employeeDao, "TEK1", "ADMIN"));
	}
	
	@Test
	public void updateRoles_InValidId_shouldThrowsException() {
		
		assertThrows(ValidationException.class,()->updateRole.assignRole(employeeDao, "xyz", "ADMIN"));
	}
	
	@Test
	public void updateRoles_validRole_doesNotThrowsException() {
		
		assertDoesNotThrow(()->updateRole.assignRole(employeeDao, "TEK1", "EMPLOYEE"));
	}
	
	@Test
	public void updateRoles_inValidRole_shouldThrowsException() {
		
		assertThrows(IllegalArgumentException.class,()->updateRole.assignRole(employeeDao, "TEK1","Man1"));
	}

}
