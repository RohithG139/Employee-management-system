package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

@ExtendWith(MockitoExtension.class)
class DeleteEmployeeServiceTest {
	
	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private DeleteEmployeeService delEmployee;
	
	@Test
	public void deleteEmployee_validId_shouldCallDao() {
		assertDoesNotThrow(() -> delEmployee.delete(employeeDao, "TEK1"));
		verify(employeeDao).deleteEmployee("TEK1");
	}
	
	@Test
	public void deleteEmployee_InvalidId_shouldThrowsException() {
		ValidationException exception=assertThrows(ValidationException.class,()->delEmployee.delete(employeeDao,"abc"));
		assertEquals("Invalid id",exception.getMessage());
	}

}
