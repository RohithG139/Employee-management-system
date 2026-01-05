package com.employees.utils;

public class ValidatorID {
	private static final String EMP_ID_REGEX = "TEK[0-9]+";
	public static boolean validId(String id) {
		return id!=null && id.matches(EMP_ID_REGEX);
	}
}
