package com.employees.model;

import java.util.Set;

import com.employees.enums.Roles;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String email;
	private String phnNo;
	private Set<Roles> roles;
	private String password;
	private boolean isActive;
	private Long deletedAt;

	//adding employee constructor
	public Employee(String name, String dept, String email, String phnNo,Set<Roles> roles,String password) {
		this.name = name;
		this.dept = dept;
		this.email = email;
		this.phnNo = phnNo;
		this.roles=roles;
		this.password=password;
//		this.isActive=isActive;
//		this.deletedAt=deletedAt;
	}
	
	//printing employee constructor
	public Employee(String id,String name, String dept, String email, String phnNo, Set<Roles> roles) {
		this.id=id;
		this.name = name;
		this.dept = dept;
		this.email = email;
		this.phnNo = phnNo;
		this.roles = roles;
		
	}
	// update admin constructor
	public Employee(String id,String name, String dept, String email, String phnNo) {
		this.id=id;
		this.name = name;
		this.dept = dept;
		this.email = email;
		this.phnNo = phnNo;
	}
	// update own details constructor
	public Employee(String id,String email,String phnNo) {
		this.id=id;
		this.email=email;
		this.phnNo=phnNo;
		
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDept() {
		return dept;
	}

	public String getEmail() {
		return email;
	}

	public String getPhnNo() {
		return phnNo;
	}

	public Set<Roles> getRole() {
		return roles;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhnNo(String phnNo) {
		this.phnNo = phnNo;
	}

	public void setRole(Set<Roles> roles) {
		this.roles = roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return "ID:" + id + "|  Name: " + name + "  |  " + "  Department: "
				+ dept + "  |  Email: " + email + "  |  Role: " + roles
				+ "  |  PhnNo : " + phnNo;
	}

}
