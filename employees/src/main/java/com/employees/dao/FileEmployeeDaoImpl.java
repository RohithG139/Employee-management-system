package com.employees.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.enums.Roles;
import com.employees.exceptions.DataAccessException;
import com.employees.model.Employee;
import com.employees.model.LoginResult;
import com.employees.utils.Util;

public class FileEmployeeDaoImpl implements EmployeeDao {
	public static final String file = "Employees.json";

	public JSONArray readDataFromJson() {
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return (JSONArray) parser.parse(reader);
		} catch (IOException e) {
			throw new DataAccessException("File read error"+e);
		} catch (ParseException e) {
			throw new DataAccessException("JSON parse error in file"+e);
		}
	}

	public void writeDataToJson(JSONArray array) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(array.toJSONString());
			writer.flush();
		} catch (IOException e) {
			throw new DataAccessException("File write error"+e);
		}
	}
	private Employee mapToEmployee(JSONObject json) {

	    String id = (String) json.get("id");
	    String name = (String) json.get("name");
	    String dept = (String) json.get("dept");
	    String email = (String) json.get("email");
	    String phnNo = (String) json.get("phnNo");
	    

	    Set<Roles> roles = new HashSet<>();
	    JSONArray rolesArray = (JSONArray) json.get("role");
	    for (Object r : rolesArray) {
	        roles.add(Roles.valueOf(r.toString()));
	    }
	    Employee emp = new Employee(id,name, dept, email, phnNo, roles);

	    return emp;
	}

	public LoginResult validateUser(String id, String password) {
		JSONArray employeesList = readDataFromJson();
		for (Object employees : employeesList) {
			JSONObject employee = (JSONObject) employees;
			
			if(employee.get("id").toString().equalsIgnoreCase(id)) {
				if(!employee.get("password").equals(password)) {
					return null;
				}
			
				JSONArray rolesArray = (JSONArray) employee.get("role");

				Set<Roles> roleList = new HashSet<>();
				for (Object roles : rolesArray) {
					roleList.add(Roles.valueOf(roles.toString()));
				}

				String empId = employee.get("id").toString();

				return new LoginResult(empId, roleList);
			}
		
		}
		return null;
	}


	
	public void addEmployee(Employee employee) {

		JSONArray employees = readDataFromJson();
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", Util.generateId(employees));
		jsonObject.put("name", employee.getName());
		jsonObject.put("dept", employee.getDept());
		jsonObject.put("email", employee.getEmail());
		jsonObject.put("phnNo", employee.getPhnNo());
		JSONArray jsonArray = new JSONArray();
		for (Roles role : employee.getRole()) {
			jsonArray.add(role.toString());
		}
		jsonObject.put("role", jsonArray);
		jsonObject.put("password", employee.getPassword());

		employees.add(jsonObject);
		writeDataToJson(employees);
		
	}

	public List<Employee> fetchEmployee() {
		List<Employee> empList=new ArrayList<>();
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			empList.add(mapToEmployee(employee));
		}
		return empList;
	}

	public Employee fetchEmployeeById(String id) {

		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				return mapToEmployee(employee);
			}
		}
		return null;
	}

	public boolean deleteEmployee(String id) {

		JSONArray employees = readDataFromJson();
	
		for (int i = 0; i < employees.size(); i++) {
			JSONObject employee = (JSONObject) employees.get(i);
			if (employee.get("id").equals(id)) {
				employees.remove(i);
				writeDataToJson(employees);
				return true;
			}
		}
		return false;

	}

	public boolean updateEmployee(Employee emp, Roles role) {
		JSONArray employees = readDataFromJson();


		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;

			if (employee.get("id").equals(emp.getId())) {

				if (role == Roles.ADMIN) {

					employee.put("name", emp.getName());
					employee.put("dept", emp.getDept());
					employee.put("email", emp.getEmail());
					employee.put("phnNo", emp.getPhnNo());
				} else {

					employee.put("phnNo", emp.getPhnNo());
					employee.put("email", emp.getEmail());
				}

				writeDataToJson(employees);
				return true;
			}
		}
		return false;
	}

	public boolean resetPassword(String id, String password) {

		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("password", password);
				writeDataToJson(employees);
				return true;
			}
		}
		return false;	
	}

	public boolean changePassword(String id, String password) {

		return resetPassword(id,password);

	}

	public boolean assignRole(String id, Roles role) {
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				JSONArray roles = (JSONArray) employee.get("role");
				if (roles.contains(role.toString())) {
					return false;
				}
				roles.add(role.toString());
				writeDataToJson(employees);
				return true;
			}
		}
		return false;
	}

	public boolean revokeRole(String id, Roles role) {
		JSONArray employees = readDataFromJson();

		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				JSONArray rolesArray = (JSONArray) employee.get("role");
				if (!rolesArray.contains(role.toString())) {
//					throw new DataAccessException("role doesnt exists for revoking role");
					return false;
				}
				rolesArray.remove(role.toString());
				writeDataToJson(employees);
				return true;
			}
		}
		return false;
	}
	

}
