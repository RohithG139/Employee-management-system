package com.employees.model;

import java.util.Set;

import com.employees.enums.Roles;


public class LoginResult {
	private static LoginResult currentUser;
    private String empId;
    private Set<Roles> roles;
    public LoginResult(String empId,Set<Roles> roles){
    		
    		this.empId=empId;
    		this.roles=roles;
    }
    
    
    
    public String getEmpId() {
    	 	return empId;
    }
    
    public Set<Roles> getRoles(){
    		return roles;
    }
}

