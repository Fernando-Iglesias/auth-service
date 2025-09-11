package com.iglesiasfernando.auth_service.unit.dto;


import com.iglesiasfernando.auth_service.dtos.SignUpRequestDTO;
import com.iglesiasfernando.auth_service.dtos.PhoneDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpRequestDTOTests {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidSignUpRequestDTO() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Ab1password2");
		dto.setPhones(List.of(
			new PhoneDTO("123456789", "11", "54")
		));

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty(), "There should be no validation errors");
	}

	@Test
	public void testBlankEmail() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("");
		dto.setPassword("Ab12password");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Email is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void testNullEmail() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail(null);
		dto.setPassword("Ab12password");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Email is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void testInvalidEmail() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("invalid-email");
		dto.setPassword("Ab12password");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Invalid email format", violations.iterator().next().getMessage());
	}

	@Test
	public void testNullPassword() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword(null);

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordTooShort() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Ab13");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must be between 8 and 12 characters",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordTooLong() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Abc12password");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must be between 8 and 12 characters",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordWithInvalidCharacters() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Ab12pass-");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordWithoutUppercaseLetters() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("ab12password");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordWithThreeUppercaseLetters() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("AbC12pass");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordWithOneDigit() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Abpassword1");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits",
			violations.iterator().next().getMessage());
	}

	@Test
	public void testPasswordWithThreeDigits() {
		SignUpRequestDTO dto = new SignUpRequestDTO();
		dto.setName("Fernando Iglesias");
		dto.setEmail("fernando@example.com");
		dto.setPassword("Ab123pass");

		Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
		assertEquals("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits",
			violations.iterator().next().getMessage());
	}
}
