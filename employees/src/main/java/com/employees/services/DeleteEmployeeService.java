package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.utils.Util;

public class DeleteEmployeeService {
	public void delete(EmployeeDao dao,String id) {
		
		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		try {
		boolean result=dao.deleteEmployee(id);
		if(!result) {
			throw new EmployeeNotFoundException("employee not found");
		}
		
		}catch(DataAccessException e) {
			throw new ServiceException("unable to delete employee:"+e.getMessage());
		}
	}
}
