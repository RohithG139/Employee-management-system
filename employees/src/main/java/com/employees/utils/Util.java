package com.employees.utils;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.employees.exceptions.IllegalEmailException;
import com.employees.exceptions.IllegalPhnNoException;

public class Util {
	public static void validateEmail(String email){
		Pattern emailPattern = Pattern.compile("^[A-Za-z09.]+@[A-Za-z0-9]+\\.[A-za-z]{2,}$");
		Matcher matcher = emailPattern.matcher(email);
			if (!matcher.matches()) {
				throw new IllegalEmailException("Invalid email");
				
			}
	}

	public static void validatePhnNo(String phnNo){
		Pattern numberPattern = Pattern.compile("^[6-9]\\d{9}$");
		Matcher matcher = numberPattern.matcher(phnNo);
		
			if (!matcher.matches()) {
				throw new IllegalPhnNoException("Invalid phnNo");
			}
		
	}
	public static String hashPassword(String password) {
		try {
			MessageDigest md=MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));

            return sb.toString();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
}
