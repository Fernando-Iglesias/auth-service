package com.iglesiasfernando.auth_service.unit;

import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceImplTests {

	@Autowired
	private AuthService authService;

	@Test
	void shouldThrowExceptionWhenEmailIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			authService.signUp(null, "password", "name", List.of());
		});

		assertEquals("Email must not be null", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenPasswordIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			authService.signUp("test@gmail.com", null, "name", List.of());
		});

		assertEquals("Password must not be null", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenPhonesIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			authService.signUp("test@gmail.com", "password", "name", null);
		});

		assertEquals("Phones must not be null", exception.getMessage());
	}

	@Test
	void shouldCreateUserWhenMinimumParamsAreValid() {
		User user = authService.signUp("test@gmail.com", "password", null, List.of());

		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals("test@gmail.com", user.getEmail());
		assertEquals("password", user.getPassword());
		assertNull(user.getName());
		assertTrue(user.getPhones().isEmpty());
		assertNotNull(user.getCreated());
		assertNull(user.getLastLogin());
		assertTrue(user.isActive());
	}
}
