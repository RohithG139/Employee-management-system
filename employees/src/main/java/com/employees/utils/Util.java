package com.employees.utils;

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
}
