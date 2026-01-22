package com.employees.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.employees.enums.Roles;

class UtilTest {

	@Test
	void validId_validId_shouldReturnTrue() {
		assertTrue(Util.validId("TEK1"));
	}

	@Test
	void validId_invalidId_shouldReturnFalse() {
		assertFalse(Util.validId("abc1"));
	}

	@Test
	void validName_validName_shouldReturnTrue() {
		assertTrue(Util.validateName("rohith"));
	}

	@Test
	void validName_invalidName_shouldReturnFalse() {
		assertFalse(Util.validateName(""));
	}

	@Test
	void validateEmail_validEmail_shouldReturnTrue() {
		assertTrue(Util.validateEmail("sunny@gmail.com"));
	}

	@Test
	void validateEmail_invalidEmail_shouldReturnFalse() {
		assertFalse(Util.validateEmail("abcgmail.com"));
	}

	@Test
	void validDept_validDept_shouldReturnTrue() {
		assertTrue(Util.validateDept("dev"));
	}

	@Test
	void validDept_invalidDept_shouldReturnFalse() {
		assertFalse(Util.validateDept(""));
	}

	@Test
	void validatePhnNo_validphnNo_shouldReturnTrue() {
		assertTrue(Util.validatePhnNo("9876543210"));
	}

	@Test
	void validatePhnNo_invalidphnNo_shouldReturnFalse() {
		assertFalse(Util.validatePhnNo("1234567890"));
	}

	@Test
	void validateRole_validRole_shouldReturnTrue() {
		assertTrue(Util.validateRole("ADMIN"));
	}

	@Test
	void validateRole_invalidRole_shouldReturnFalse() {
		assertFalse(Util.validateRole("ADMIN1"));
	}

	@Test
	void hashPassword_validPassword_shouldReturnHashedValue() {
		String hashed = Util.hashPassword("Tek@123");
		assertNotNull(hashed);
		assertNotEquals("Tek@123", hashed);
	}

	@Test
	void generatePassword_shouldGenerateSixCharPassword() {
		String password = Util.generatePassword();
		assertNotNull(password);
		assertEquals(6, password.length());
	}

}
