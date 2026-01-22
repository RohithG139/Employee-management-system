package com.employees.services;

import com.employees.dao.EmployeeDao;
import com.employees.enums.Roles;
import com.employees.exceptions.ValidationException;
import com.employees.utils.Util;

public class UpdateRolesService {
	public void assignRole(EmployeeDao dao, String id, String role) {

		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			throw new IllegalArgumentException("Invalid role");
		}
		dao.assignRole(id, Roles.valueOf(role));
	}

	public void revokeRole(EmployeeDao dao, String id, String role) {
		if (!Util.validId(id)) {
			throw new ValidationException("Invalid id");
		}
		if (!Util.validateRole(role)) {
			throw new IllegalArgumentException("Invalid role");
		}
		dao.revokeRole(id, Roles.valueOf(role));
	}
}
