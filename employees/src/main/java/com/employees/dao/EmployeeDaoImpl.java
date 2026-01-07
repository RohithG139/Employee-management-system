package com.employees.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.model.Employee;
import com.employees.security.Roles;
import com.employees.utils.PasswordGenerator;
import com.employees.utils.Util;


public class EmployeeDaoImpl implements EmployeeDao {
	PasswordGenerator passObj = new PasswordGenerator();
	public static Set<Roles> roles;
	public static String id;
	public static final String file="Employees.json";
	Util util =new Util();
	public JSONArray readDataFromJson() throws ParseException, IOException{
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return (JSONArray) parser.parse(reader);
		}
	}

	public void writeDataToJson(JSONArray array) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(array.toJSONString());
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean validateUser(String name, String password) throws ParseException, IOException {
			JSONArray employeesList = readDataFromJson();
			for (Object employees : employeesList) {
				JSONObject employee = (JSONObject) employees;

				if (employee.get("name").toString().equalsIgnoreCase(name)
						&& employee.get("password").equals(Util.hashPassword(password))) {

					JSONArray rolesArray = (JSONArray) employee.get("role");

					Set<Roles> roleList = new HashSet<>();
					for (Object roles : rolesArray) {
						roleList.add(Roles.valueOf(roles.toString()));
					}

					EmployeeDaoImpl.roles = roleList;
					EmployeeDaoImpl.id = employee.get("id").toString();

					return true;
				}
			}
		
		return false;
	}

	public boolean checkEmp(JSONArray array, String id) {
		for (Object obj : array) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				return true;
			}
		}
		return false;
	}

	public static void print(JSONObject emp) {
		System.out.println("ID:" + emp.get("id") + "|  Name: " + emp.get("name") + "  |  " + "  Department: "
				+ emp.get("dept") + "  |  Email: " + emp.get("email") + "  |  Role: " + emp.get("role")
				+ "  |  PhnNo : " + emp.get("phnNo"));
	}

	public void addEmployee(Employee employee) throws ParseException, IOException {
		
		JSONArray employees = readDataFromJson();
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", employee.getId());
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

	public void fetchEmployee() throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			print(employee);
		}
		
	}

	public void fetchEmployeeById(String id) throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				print(employee);
				return;
			}
		}
	}

	public void deleteEmployee(String id) throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (int i = 0; i < employees.size(); i++) {
			JSONObject employee = (JSONObject) employees.get(i);
			if (employee.get("id").equals(id)) {
				employees.remove(i);
				writeDataToJson(employees);
				return;
			}
		}

	}

	public void updateEmployee(String id, String name,String dept,String email,String phnNo) throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("name", name);
				employee.put("dept", dept);
				employee.put("email", email);
				employee.put("phnNo", phnNo);
				writeDataToJson(employees);
				return;
			}
		}
	}

	public void resetPassword(String id, String password) throws ParseException, IOException {

		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("password", password);
				writeDataToJson(employees);
				return;
			}
		}

	}

	public void changePassword(String id, String password) throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("password", password);
				writeDataToJson(employees);
				return;
			}
		}
	}

	public void updateUserLogin(String id, String phnNo) throws ParseException, IOException {
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("phnNo", phnNo);
				writeDataToJson(employees);
				return;
			}
		}
		

	}
}
