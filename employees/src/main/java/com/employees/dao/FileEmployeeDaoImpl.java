package com.employees.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.employees.exceptions.EmployeeNotFoundException;
import com.employees.exceptions.ValidationException;
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
			throw new DataAccessException("File read error" + e);
		} catch (ParseException e) {
			throw new DataAccessException("JSON parse error in file" + e);
		}
	}

	public void writeDataToJson(JSONArray array) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(array.toJSONString());
			writer.flush();
		} catch (IOException e) {
			throw new DataAccessException("File write error" + e);
		}
	}

	private JSONObject findEmployee(JSONArray data, String id) {
		for (Object obj : data) {
			JSONObject emp = (JSONObject) obj;
			if (Boolean.TRUE.equals(emp.get("isActive")) && emp.get("id").equals(id)) {
				return emp;
			}
		}
		return null;
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
		Employee emp = new Employee(id, name, dept, email, phnNo, roles);

		return emp;
	}

	public LoginResult validateUser(String id, String password) {
		JSONArray employeesList = readDataFromJson();

		for (Object obj : employeesList) {
			JSONObject employee = (JSONObject) obj;

			if (employee.get("id").toString().equalsIgnoreCase(id) && Boolean.TRUE.equals(employee.get("isActive"))) {

				if (!employee.get("password").equals(password)) {
					return null;
				}

				JSONArray rolesArray = (JSONArray) employee.get("role");
				Set<Roles> roleList = new HashSet<>();
				for (Object r : rolesArray) {
					roleList.add(Roles.valueOf(r.toString()));
				}

				return new LoginResult(id, roleList);
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

		jsonObject.put("isActive", true);
		jsonObject.put("deletedAt", null);
		employees.add(jsonObject);
		writeDataToJson(employees);

	}

	public List<Employee> fetchEmployee() {
		List<Employee> empList = new ArrayList<>();
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (Boolean.TRUE.equals(employee.get("isActive")))
				empList.add(mapToEmployee(employee));
		}
		return empList;
	}

	public Employee fetchEmployeeById(String id) {

		JSONArray data = readDataFromJson();
		JSONObject emp = findEmployee(data, id);
		return emp == null ? null : mapToEmployee(emp);
	}

	public void deleteEmployee(String id) {

		JSONArray data = readDataFromJson();
		JSONObject emp = findEmployee(data, id);

		if (emp == null) {
			throw new EmployeeNotFoundException("employee not found");
		}

		String deletedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		emp.put("isActive", false);
		emp.put("deletedAt", deletedAt);
		writeDataToJson(data);

	}

	public void updateEmployee(Employee emp, Roles role) {
		JSONArray data = readDataFromJson();

		JSONObject json = findEmployee(data, emp.getId());

		if (json == null) {
			throw new EmployeeNotFoundException("employee not found");
		}

		if (role == Roles.ADMIN) {
			json.put("name", emp.getName());
			json.put("dept", emp.getDept());
			json.put("email", emp.getEmail());
			json.put("phnNo", emp.getPhnNo());
		} else {
			json.put("email", emp.getEmail());
			json.put("phnNo", emp.getPhnNo());
		}

		writeDataToJson(data);
	}

	public void resetPassword(String id, String password) {

		JSONArray data = readDataFromJson();
		JSONObject emp = findEmployee(data, id);

		if (emp == null) {
			throw new EmployeeNotFoundException("employee not found");
		}

		emp.put("password", password);
		writeDataToJson(data);

	}

	public void changePassword(String id, String password) {

		resetPassword(id, password);

	}

	public void assignRole(String id, Roles role) {
		JSONArray data = readDataFromJson();
		JSONObject emp = findEmployee(data, id);

		if (emp == null) {
			throw new EmployeeNotFoundException("employee not found");
		}

		JSONArray roles = (JSONArray) emp.get("role");
		if (roles.contains(role.toString())) {
			throw new ValidationException("duplicate role assigned");
		}

		roles.add(role.toString());
		writeDataToJson(data);
	}

	public void revokeRole(String id, Roles role) {
		JSONArray data = readDataFromJson();
		JSONObject emp = findEmployee(data, id);

		if (emp == null) {
			throw new EmployeeNotFoundException("employee not found");
		}

		JSONArray roles = (JSONArray) emp.get("role");
		if (!roles.contains(role.toString())) {
			throw new ValidationException("role not assigned");
		}

		roles.remove(role.toString());
		writeDataToJson(data);
	}

	public List<Employee> fetchInActiveEmployees() {
		List<Employee> empList = new ArrayList<>();
		JSONArray employees = readDataFromJson();
		for (Object obj : employees) {
			JSONObject employee = (JSONObject) obj;
			if (Boolean.FALSE.equals(employee.get("isActive")))
				empList.add(mapToEmployee(employee));
		}
		return empList;
	}
}
