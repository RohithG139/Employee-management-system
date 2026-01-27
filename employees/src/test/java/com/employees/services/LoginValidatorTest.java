package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.ValidationException;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

@ExtendWith(MockitoExtension.class)
class LoginValidatorTest {

	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private LoginValidator login;

	@Test
	public void loginSuccess() {
		LoginResult mockUser=new LoginResult("TEK1",Set.of(Roles.EMPLOYEE));
		when(employeeDao.validateUser("TEK1", Util.hashPassword("pass"))).thenReturn(mockUser);
		
		LoginResult user=login.login(employeeDao, "TEK1", "pass");
		assertEquals("TEK1",user.getEmpId());
		
		verify(employeeDao).validateUser("TEK1",Util.hashPassword("pass"));
	}
	
	@Test
	public void loginFailure() {
		
		when(employeeDao.validateUser("TEK1", Util.hashPassword("pass"))).thenReturn(null);
		
		ValidationException exception=assertThrows(ValidationException.class,()->login.login(employeeDao, "TEK1","pass"));
		
		assertEquals("Incorrect username or password",exception.getMessage());
		verify(employeeDao).validateUser("TEK1",Util.hashPassword("pass"));
	}
	
	
}
