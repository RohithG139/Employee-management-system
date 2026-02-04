package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
@ExtendWith(MockitoExtension.class)
class PasswordOperationsTest {

	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private LoginService operations;
	
	@Test
	public void resetPassword_validId_shouldReturnPassword() {
		
		String password=operations.resetPasswordService(employeeDao,"TEK1");
		assertNotNull(password);
		verify(employeeDao).resetPassword(eq("TEK1"),anyString());
	}
	
	@Test
	public void resetPassword_IdNotFound_shouldThrowsException() {
		doThrow(new EmployeeNotFoundException("employee not found")).when(employeeDao).resetPassword(anyString(),anyString());
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class,
				()->operations.resetPasswordService(employeeDao, "TEK9"));
		assertEquals("employee not found",exception.getMessage());
		verify(employeeDao,never()).resetPassword(eq("TEK1"),anyString());
	}
	
	@Test
	public void changePassword_validId_shouldCallDao() {
		
		assertDoesNotThrow(()->operations.changePasswordService(employeeDao,"TEK1","Pass@123"));
		verify(employeeDao).changePassword(eq("TEK1"), anyString());
	}
	
	@Test
	public void changePassword_InvalidId_shouldCallDao() {
		
		assertThrows(ValidationException.class,()->operations.changePasswordService(employeeDao,"xyz1","Pass@123"));
		verify(employeeDao,never()).changePassword(anyString(), anyString());
	}
	
	@Test
	public void changePassword_InvalidPassword_shouldCallDao() {
		
		assertThrows(ValidationException.class,()->operations.changePasswordService(employeeDao,"xyz1","pass"));
		verify(employeeDao,never()).changePassword(anyString(), anyString());
	}
	

}
