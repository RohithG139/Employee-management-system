package com.employees.utils;

import java.util.UUID;

public class PasswordGenerator {
	public static String generate() {
		return UUID.randomUUID().toString().substring(0,6);
	}
}
