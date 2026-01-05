package com.employees.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermission {
	Map<String , Set<Operations>> map=new HashMap<>(); 
	
	public RolePermission() {
		map.put("ADMIN",EnumSet.of(
				Operations.ADD,
				Operations.UPDATE,
				Operations.FETCH,
				Operations.FETCHBYID,
				Operations.DELETE,
				Operations.EXIT
				));
		
		map.put("MANAGER", EnumSet.of(
				Operations.FETCH,
				Operations.FETCHBYID,
				Operations.UPDATE,
				Operations.EXIT
				));
		
		map.put("EMPLOYEE", EnumSet.of(
				Operations.FETCHBYID,
				Operations.UPDATE,
				Operations.EXIT
				));
	}
	
	public boolean hasAccess(String role,Operations operation) {
		return map.get(role).contains(operation);
	}
}
