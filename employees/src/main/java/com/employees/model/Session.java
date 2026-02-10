package com.employees.model;

import java.util.Set;

import com.employees.enums.Roles;

public class Session {
	private final LoginResult currentUser;
	
	public Session(LoginResult currentUser){
		this.currentUser=currentUser;
	}
	
	public String getId() {
		return currentUser.getEmpId();
	}
	
	public Set<Roles> getRoles(){
		return currentUser.getRoles();
	}
	
	public LoginResult getUser() {
		return currentUser;
	}
}


