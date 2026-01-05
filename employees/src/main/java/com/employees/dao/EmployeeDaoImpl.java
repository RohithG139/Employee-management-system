package com.employees.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.model.Employee;
import com.employees.security.Roles;
import com.employees.utils.PasswordGenerator;
import com.employees.utils.Util;

public class EmployeeDaoImpl implements EmployeeDao {
	File file = new File("src/main/resources/Employees.json");
	PasswordGenerator passObj=new PasswordGenerator();
	public JSONArray readJson() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return (JSONArray) parser.parse(reader);
		}
	}

	public void writeJson(JSONArray array) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(array.toJSONString());
		}
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

	public void addEmployee(Employee e) throws ParseException, IOException {
		JSONArray employees = readJson();
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", e.getId());
		jsonObject.put("name", e.getName());
		jsonObject.put("dept", e.getDept());
		jsonObject.put("email", e.getEmail());
		jsonObject.put("phnNo", e.getPhnNo());
		JSONArray jsonArray=new JSONArray();
		for(Roles r:e.getRole()) {
			jsonArray.add(r.toString());
		}
		jsonObject.put("role", jsonArray);
		jsonObject.put("password", e.getPassword());

		employees.add(jsonObject);
		writeJson(employees);
	}

	public void fetchEmployee() throws ParseException, IOException {
		JSONArray employees = readJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			print(employee);
		}
	}

	public void fetchEmployeeById(String id) throws ParseException, IOException {
		JSONArray employees = readJson();
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
		JSONArray employees = readJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (int i = 0; i < employees.size(); i++) {
			JSONObject employee = (JSONObject) employees.get(i);
			if (employee.get("id").equals(id)) {
				employees.remove(i);
				writeJson(employees);
				return;
			}
		}

	}

	public void updateEmployee(String id, String name) throws ParseException, IOException {
		JSONArray employees = readJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("name", name);
				writeJson(employees);
				return;
			}
		}
	}
	
	public void resetPassword(String id,String password) throws ParseException, IOException{
		
		JSONArray employees = readJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				 employee.put("password",password);
				 writeJson(employees);
				 return;
				}
			}
		
	}
	public void changePassword(String id,String password) throws ParseException, IOException {
		JSONArray employees = readJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				employee.put("password", password);
				writeJson(employees);
				return;
			}
		}
	}

}
