package com.employees.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.model.Employee;
import com.employees.utils.Util;
public class ValidateLogin {
	public static Set<Roles> roles;
	public static String id;
	public boolean validate(String id,String password) {
		File file = new File("src/main/resources/Employees.json");
		JSONParser parser=new JSONParser();
		Employee emp=new Employee();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Object obj = parser.parse(reader);
            JSONArray employeesList = (JSONArray) obj;
            for (Object o : employeesList) {
                JSONObject employee = (JSONObject) o;
                
                if (employee.get("id").equals(id) && employee.get("password").equals(Util.hashPassword(password))) {
                	
                	   JSONArray rolesArray = (JSONArray) employee.get("role");
                	   
                	   Set<Roles> roleList = new HashSet<>();
                	   for (Object r : rolesArray) {
                	       roleList.add(Roles.valueOf(r.toString()));
                	   }

                	   ValidateLogin.roles = roleList;
                	   ValidateLogin.id = id;

                   return true;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Parsing Error: " + e.getMessage());
        }
        
        return false;
    }
}
