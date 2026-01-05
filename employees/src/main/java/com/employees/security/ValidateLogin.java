package com.employees.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class ValidateLogin {
	public String role;
	public boolean validate(String id,String password) {
		File file = new File("src/main/resources/Employees.json");
		JSONParser parser=new JSONParser();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Object obj = parser.parse(reader);
            JSONArray employeesList = (JSONArray) obj;
            for (Object o : employeesList) {
                JSONObject employee = (JSONObject) o;
                
                if (id.equals(employee.get("id")) && password.equals(employee.get("password"))) {
                	   role=(String)employee.get("role");
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
