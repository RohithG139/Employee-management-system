package com.employees.exceptions;

public class AuthorizedException extends RuntimeException {
	public AuthorizedException(String msg) {
		super(msg);
	}
}
