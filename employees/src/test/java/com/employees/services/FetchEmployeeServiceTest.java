package com.employees.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
class FetchEmployeeServiceTest {
	@Mock
	private EmployeeDao employeeDao;
	
	@InjectMocks
	private EmployeeService fetchEmp;
	
	@Test
	public void employeeFetchOwnData_shouldCallDao(){
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.EMPLOYEE);
		LoginResult user=new LoginResult("TEK1",roles);
		
		fetchEmp.fetchById(employeeDao, "TEK1", user);
		verify(employeeDao).fetchEmployeeById("TEK1");
	}
	
	@Test
	public void employeeFetchOtherData_shouldThrowsException(){
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.EMPLOYEE);
		LoginResult user=new LoginResult("TEK1",roles);
		assertThrows(AuthorizedException.class,()->fetchEmp.fetchById(employeeDao, "TEK2", user));
		
		verify(employeeDao,never()).fetchEmployeeById("TEK2");
	}
	
	@Test
	public void AdminfetchEmployeeData_shouldCallDao(){
		Employee emp=new Employee("id","name","dept","email",Set.of(Roles.EMPLOYEE),"phnNo");
		when(employeeDao.fetchEmployeeById("TEK12")).thenReturn(emp);
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ADMIN);
		LoginResult user=new LoginResult("TEK1",roles);
		
		fetchEmp.fetchById(employeeDao, "TEK12", user);
		verify(employeeDao).fetchEmployeeById("TEK12");
	}
	
	@Test
	public void ManagerfetchEmployeeData_shouldCallDao(){
		Employee emp=new Employee("id","name","dept","email",Set.of(Roles.EMPLOYEE),"phnNo");
		when(employeeDao.fetchEmployeeById("TEK10")).thenReturn(emp);
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.MANAGER);
		LoginResult user=new LoginResult("TEK1",roles);
		fetchEmp.fetchById(employeeDao, "TEK10", user);
		verify(employeeDao).fetchEmployeeById("TEK10");
	}

	@Test
	public void AdminfetchEmployeeData_shouldThrowsException(){
		when(employeeDao.fetchEmployeeById("TEK12")).thenReturn(null);
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ADMIN);
		LoginResult user=new LoginResult("TEK1",roles);

		assertThrows(EmployeeNotFoundException.class,()->fetchEmp.fetchById(employeeDao, "TEK12", user));
		verify(employeeDao).fetchEmployeeById("TEK12");
	}
	
	@Test
	public void fetchEmployee_InvalidId_shouldThrowsException(){
		Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ADMIN);
		LoginResult user=new LoginResult("TEK1",roles);
		assertThrows(ValidationException.class,()->fetchEmp.fetchById(employeeDao, "abc1", user));
		verify(employeeDao,never()).fetchEmployeeById("abc1");
	}
	
}
