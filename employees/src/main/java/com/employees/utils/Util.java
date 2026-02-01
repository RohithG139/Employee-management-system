package com.employees.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.employees.enums.Roles;

public class Util {
	private static final String EMP_ID_REGEX = "TEK[0-9]+";
	private static final String PREFIX = "TEK";
	
	public static boolean validId(String id) {
		return id != null && id.matches(EMP_ID_REGEX);
	}

	public static boolean validateEmail(String email) {
		if ( email!= null && email.trim().isEmpty()) {
			return false;
		}
		Pattern emailPattern = Pattern.compile("^[A-Za-z0-9.]+@[A-Za-z0-9]+\\.[A-za-z]{2,}$");
		Matcher matcher = emailPattern.matcher(email);
		if (!matcher.matches()) {
			return false;

		}
		return true;
	}

	public static boolean validatePhnNo(String phnNo) {
		Pattern numberPattern = Pattern.compile("^[6-9]\\d{9}$");
		Matcher matcher = numberPattern.matcher(phnNo);

		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	public static boolean validateName(String name) {
		if (name != null && name.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean validateDept(String dept) {
		if (dept != null && dept.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean validatePassword(String password) {
		Pattern passwordPattern=Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=]).{8,}$");
		Matcher matcher=passwordPattern.matcher(password);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	public static boolean validateRole(String role) {
		if (role == null || role.trim().isEmpty()) {
			return false;
		}
		try {
			Roles.valueOf(role.toUpperCase().trim());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(password.getBytes());

			StringBuilder sb = new StringBuilder();
			for (byte b : bytes)
				sb.append(String.format("%02x", b));

			return sb.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static String generatePassword() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public static Connection getConnection() {
		try {
			Properties props = new Properties();

			InputStream input = Util.class.getClassLoader().getResourceAsStream("Application.properties");

			if (input == null) {
				throw new RuntimeException("Application.properties not found");
			}

			props.load(input);

			String url = props.getProperty("db.url");
			String user = props.getProperty("db.username");
			String pass = props.getProperty("db.password");

			return DriverManager.getConnection(url, user, pass);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	

	    public static String generateId(JSONArray employees) {

	        if (employees == null || employees.isEmpty()) {
	            return PREFIX + "1";
	        }

	        JSONObject last = (JSONObject) employees.get(employees.size() - 1);
	        String lastId = (String) last.get("id");

	        int num = 1;
	        if (lastId != null && lastId.startsWith(PREFIX)) {
	            num = Integer.parseInt(lastId.substring(PREFIX.length())) + 1;
	        }
	        return PREFIX + num;
	    }
}
