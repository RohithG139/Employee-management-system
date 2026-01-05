package com.employees.exceptions;

public class IllegalEmailException extends RuntimeException {
	public IllegalEmailException(String msg) {
		super(msg);
	}
}
