package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.exceptions.ValidationException;
import com.employees.utils.Util;

public class DeleteEmployeeService {
	public void delete(EmployeeDao dao,String id) {
		
		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}

		dao.deleteEmployee(id);
		dao.fetchEmployee();
	}
}
