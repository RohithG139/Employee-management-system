package com.employees.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.AuthorizedException;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class EmployeeService {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public void insert(EmployeeDao dao, Employee employee, String role) {
		logger.info("add employee request recieved for name {}",employee.getName());
		if (!Util.validateName(employee.getName())) {
			logger.warn("validation failed: invalid name for name {}",employee.getName());
			throw new ValidationException("Invalid name ");
		}
		if (!Util.validateDept(employee.getDept())) {
			logger.warn("validation failed: invalid dept for name {}",employee.getName());
			throw new ValidationException("Invalid department");
		}
		if (!Util.validateEmail(employee.getEmail())) {
			logger.warn("validation failed: invalid email for name {}",employee.getName());
			throw new ValidationException("Invalid email");
		}
		if (!Util.validatePhnNo(employee.getPhnNo())) {
			logger.warn("validation failed: invalid phnNo for name {}",employee.getName());
			throw new ValidationException("Invalid phone number");
		}
		if (!Util.validateRole(role)) {
			logger.warn("validation failed: invalid role for name {}",employee.getName());
			throw new ValidationException("Invalid role");
		} else {
			Set<Roles> roleList = new HashSet<>();
			roleList.add(Roles.valueOf(role));
			employee.setRole(roleList);
		}
		try {
			dao.addEmployee(employee);
			logger.info("employee added succesfully with name {}",employee.getName());
		} catch (DataAccessException e) {
			logger.error("Database error during adding employee with name {}",employee.getName(),e);
			throw new ServiceException("unable to add employee:" + e.getMessage());
		}

	}
	
	public void delete(EmployeeDao dao,String id) {
		logger.info("delete employee request recieved for id {}",id);
		if (!Util.validId(id)) {
			logger.warn("validation failed: invalid id for id {}",id);
			throw new ValidationException("Invalid id");
		}
		try {
		dao.deleteEmployee(id);
		logger.info("Employee deleted succesfully for employee id {}",id);
		}catch(DataAccessException e) {
			logger.error("Database error during delete employee with id {} ",id,e);
			throw new ServiceException("unable to delete employee:"+e.getMessage());
		}catch(EmployeeNotFoundException e) {
			throw e;
		}
	}
	
	public List<Employee> fetchAll(EmployeeDao dao) {
		logger.info("fetch all employees request recieved");
		List<Employee> employees=new ArrayList<>();
		try {
			employees=dao.fetchEmployee();
			logger.info("fetch all the employees successfully");
		}catch (DataAccessException e) {
			logger.error("Database error while fetch employees",e);
			throw new ServiceException("unable to fetch employee:"+e.getMessage());
		}
		return employees;
	}

	public Employee fetchById(EmployeeDao dao, String id, LoginResult currentUser) {
		Employee emp = null;
		logger.info("fetch the employee request recieved for id {} ",id);
		if (currentUser.getRoles().size() == 1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {
			if (!id.equals(currentUser.getEmpId())) {
				logger.warn("Authorized error: user {} tried to access employee with id {} ",currentUser.getEmpId(),id);
				throw new AuthorizedException("Employee can not view other employees");
			}
			try {
				emp = dao.fetchEmployeeById(currentUser.getEmpId());
				logger.info("fetch the employee successfully with id {}",id);
			} catch (DataAccessException e) {
				logger.error("Database error while fetch the employee with id {} ",id,e);
				throw new ServiceException("unable to fetch employee:"+e.getMessage());
			}
		}

		else {
			if (!Util.validId(id)) {
				logger.warn("Validation error: Invalid id for id {} ",id);
				throw new ValidationException("Invalid id");
			}
			try {
				emp = dao.fetchEmployeeById(id);
				if (emp == null) {
					logger.warn("employee not found with id {} ",id);
					throw new EmployeeNotFoundException("employee not found");
				}
				logger.info("fetch the employee successfully with id {}",id);
			} catch (DataAccessException e) {
				logger.error("Database error while fetch the employee with id {} ",id,e);
				throw new ServiceException("unable to fetch employee:"+e.getMessage());
			}
		}

		return emp;
	}
	
	public void update(EmployeeDao dao, Employee employee, LoginResult currentUser) {
		logger.info("update employee request recieved for id {} ",employee.getId());
		if (currentUser.getRoles().size() == 1 && currentUser.getRoles().contains(Roles.EMPLOYEE)) {

			if (!employee.getId().equals(currentUser.getEmpId())) {
				logger.warn("Authorized error: user {} tried to update employee with id {} ",currentUser.getEmpId(),employee.getId());
				throw new AuthorizedException("Employees can update only their own details");
			}
			if (!Util.validateEmail(employee.getEmail())) {
				logger.warn("validation failed: invalid email for id {}",employee.getId());
				throw new ValidationException("Invalid email");
			}
			if (!Util.validatePhnNo(employee.getPhnNo())) {
				logger.warn("validation failed: invalid phnNo for id {}",employee.getId());
				throw new ValidationException("Invalid phnNo");
			}

			try {
				dao.updateEmployee(employee, Roles.EMPLOYEE);
				logger.info("update employee successfully for id {} ",employee.getId());
			} catch (DataAccessException e) {
				logger.error("Database error while update the employee for id {} ",employee.getId(),e);
				throw new ServiceException("unable to update employee:" + e.getMessage());
			}
		} else {
			if (!Util.validId(employee.getId())) {
				logger.warn("validation failed: invalid id for {}",employee.getId());
				throw new ValidationException("Invalid id");
			}

			if (!Util.validateName(employee.getName())) {
				logger.warn("validation failed: invalid name for {}",employee.getId());
				throw new ValidationException("Invalid name");
			}
			if (!Util.validateDept(employee.getDept())) {
				logger.warn("validation failed: invalid dept for {}",employee.getId());
				throw new ValidationException("Invalid department");
			}
			if (!Util.validateEmail(employee.getEmail())) {
				logger.warn("validation failed: invalid email for {}",employee.getId());
				throw new ValidationException("Invalid email");
			}
			if (!Util.validatePhnNo(employee.getPhnNo())) {
				logger.warn("validation failed: invalid phnNo for {}",employee.getId());
				throw new ValidationException("Invalid phnNo");
			}
			try {
				 dao.updateEmployee(employee, Roles.ADMIN);

			} catch (DataAccessException e) {
				logger.error("Database error while update the employee for id {} ",employee.getId(),e);
				throw new ServiceException("unable to update employee:" + e.getMessage());
			}catch(EmployeeNotFoundException e) {
				throw e;
			}

		}
	}
	
	public void assignRole(EmployeeDao dao, String id, String role) {
		logger.info("assign role {} request recieved for id {} ",role,id);
		if (!Util.validId(id)) {
			logger.warn("validation failed: invalid id for id{}",id);
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			logger.warn("validation failed: invalid role for id{}",role);
			throw new ValidationException("Invalid role");
		}
		try {
			dao.assignRole(id, Roles.valueOf(role));
			logger.info("assign role {} succesfully to id {} ",role,id);
		} catch (DataAccessException e) {
			logger.error("Database error while assign the role for id {} ",id,e.getMessage());
			throw new ServiceException("unable to assign role:"+e);
		}catch (EmployeeNotFoundException | ValidationException e) {
	        throw e;
		}
	}

	public void revokeRole(EmployeeDao dao, String id, String role) {
		logger.info("revoke role {} request recieved for id {} ",role,id);
		if (!Util.validId(id)) {
			logger.warn("validation failed: invalid id for id{}",id);
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			logger.warn("validation failed: invalid role for id{}",role);
			throw new ValidationException("Invalid role");
		}
		try {
			dao.revokeRole(id, Roles.valueOf(role));

			logger.info("revoke role {} succesfully to id {} ",role,id);
		} catch (DataAccessException e) {
			logger.error("Database error while revoke the role for id {} ",id,e);
			throw new ServiceException("unable to revoke role:"+e.getMessage());
		}catch (EmployeeNotFoundException | ValidationException e) {
	        throw e;
		}
		
	}
	
	public List<Employee> fetchInActiveEmployees(EmployeeDao dao) {
		logger.info("fetch InActive employees request recieved");
		List<Employee> employees=new ArrayList<>();
		try {
			employees=dao.fetchInActiveEmployees();
			logger.info("fetch inactive employees successfully");
		}catch (DataAccessException e) {
			logger.error("Database error while fetch employees",e);
			throw new ServiceException("unable to fetch employee:"+e.getMessage());
		}
		return employees;
	}
}
