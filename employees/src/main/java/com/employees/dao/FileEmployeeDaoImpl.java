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
import com.employees.model.LoginResult;
import com.employees.security.Roles;
import com.employees.utils.Util;

public class FileEmployeeDaoImpl implements EmployeeDao {
	public static final String file = "Employees.json";
	ServerValidations validations = new ServerValidations();

	Util util = new Util();

	public JSONArray readDataFromJson() {
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return (JSONArray) parser.parse(reader);
		} catch (IOException e) {
			System.out.println("IO Error:" + e.getMessage());
		} catch (ParseException e) {
			System.out.println("Parser Error:" + e.getMessage());
		}
		return null;
	}

	public void writeDataToJson(JSONArray array) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(array.toJSONString());
			writer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public LoginResult validateUser(String id, String password) {
		JSONArray employeesList = readDataFromJson();
		for (Object employees : employeesList) {
			JSONObject employee = (JSONObject) employees;

			if (employee.get("id").toString().equalsIgnoreCase(id)
					&& employee.get("password").equals(Util.hashPassword(password))) {

				JSONArray rolesArray = (JSONArray) employee.get("role");

				Set<Roles> roleList = new HashSet<>();
				for (Object roles : rolesArray) {
					roleList.add(Roles.valueOf(roles.toString()));
				}

				String empId = employee.get("id").toString();

				return new LoginResult(true, empId, roleList);
			}
		}

		return new LoginResult(false, null, null);
	}

	private boolean checkEmp(JSONArray array, String id) {
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

	public void addEmployee(Employee employee) {

		JSONArray employees = readDataFromJson();
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", validations.generateId(employees));
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
		System.out.println("Employee added succesfully");
	}

	public void fetchEmployee() {

		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			print(employee);
		}
	}

	public void fetchEmployeeById(String id) {

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

	public void deleteEmployee(String id) {

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

	public void updateEmployee(Employee emp, Roles role) {
		JSONArray employees = readDataFromJson();

		if (!checkEmp(employees, emp.getId())) {
			System.out.println("Employee does not exist.");
			return;
		}

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
				}

				writeDataToJson(employees);
				System.out.println("Employee updated successfully.");
				return;
			}
		}
	}

	public void resetPassword(String id, String password) {

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
				System.out.println("password reset succesfully.");
				return;
			}
		}

	}

	public void changePassword(String id, String password) {

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
				System.out.println("password changed succesfully.");
				return;
			}
		}

	}

	public void assignRole(String id,Roles role) {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				JSONArray roles=(JSONArray) employee.get("role");
				if (roles.contains(role.toString())) {
				    System.out.println("role already exist");
				    return;
				}
				roles.add(role.toString());
				writeDataToJson(employees);
				System.out.println("role assigned succesfully");
				return;
			}
		}
		
	}

	public void revokeRole(String id,Roles role) {
		JSONArray employees = readDataFromJson();
		if (!checkEmp(employees, id)) {
			System.out.println("Employee not exist:");
			return;
		}
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (employee.get("id").equals(id)) {
				JSONArray roles=(JSONArray) employee.get("role");
				if (!roles.contains(role.toString())) {
				    System.out.println("role not exist");
				    return;
				}
				roles.remove(role.toString());
				writeDataToJson(employees);
				System.out.println("role revoked succesfully");
				return;
			}
		}
	}

}
