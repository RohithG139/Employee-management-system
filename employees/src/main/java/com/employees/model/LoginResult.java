package com.employees.model;

import java.util.Set;

import com.employees.security.Roles;

public class LoginResult {
    private boolean success;
    private String empId;
    private Set<Roles> roles;
    public LoginResult(boolean success,String empId,Set<Roles> roles){
    		this.success=success;
    		this.empId=empId;
    		this.roles=roles;
    }
    
    public boolean getSuccess() {
    		return success;
    }
    
    public String getEmpId() {
    	 	return empId;
    }
    
    public Set<Roles> getRoles(){
    		return roles;
    }
}

