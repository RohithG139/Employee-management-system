package com.employees.model;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String email;
	private String phnNo;
	private String role;
	private String password;

	public Employee(String id, String name, String dept, String email, String phnNo, String role, String password) {
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.email = email;
		this.phnNo = phnNo;
		this.role = role;
		this.password = password;
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
	
	public String getRole() {
		return role;
	}
	
	public String getPassword() {
		return password;
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
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String toString() {
		return "Employee{id=" + id + ", name='" + name + "', dept='" + dept + "'}";
	}
}
