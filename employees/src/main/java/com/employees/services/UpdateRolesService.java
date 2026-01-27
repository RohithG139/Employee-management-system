package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.DataAccessException;
import com.employees.exceptions.ServiceException;
import com.employees.exceptions.ValidationException;
import com.employees.utils.Util;

public class UpdateRolesService {
	public void assignRole(EmployeeDao dao, String id, String role) {

		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			throw new ValidationException("Invalid role");
		}
		try {
			boolean result = dao.assignRole(id, Roles.valueOf(role));

			if (!result) {
				throw new ValidationException("duplicate role assigned");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("unable to assign role:"+e.getMessage());
		}
	}

	public void revokeRole(EmployeeDao dao, String id, String role) {
		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			throw new ValidationException("Invalid role");
		}
		try {
			boolean result = dao.revokeRole(id, Roles.valueOf(role));

			if (!result) {
				throw new ValidationException("role doesnt exist");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("unable to revoke role:"+e.getMessage());
		}
		
	}
}
