package com.iglesiasfernando.auth_service.integration;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.exceptions.EmailAlreadyExistsException;
import com.iglesiasfernando.auth_service.exceptions.InvalidCredentialsException;
import com.iglesiasfernando.auth_service.repositories.UserRepository;
import com.iglesiasfernando.auth_service.services.AuthService;
import com.iglesiasfernando.auth_service.services.AuthServiceImpl;
import com.iglesiasfernando.auth_service.utils.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceImplIntegrationTests {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	void signUpSignUpShouldThrowExceptionWhenEmailIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			authService.signUp(null, "Meowmeow89", "Snowball", List.of())
		);

		assertEquals("Email must not be null", exception.getMessage());
	}

	@Test
	void signUpShouldThrowExceptionWhenPasswordIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			authService.signUp("cat@meow.com", null, "Snowball", List.of())
		);

		assertEquals("Password must not be null", exception.getMessage());
	}

	@Test
	void signUpShouldThrowExceptionWhenPhonesIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				authService.signUp("cat@meow.com", "Meowmeow89", "Snowball", null)
		);

		assertEquals("Phones must not be null", exception.getMessage());
	}

	@Test
	void signUpShouldCreateUserAndTokenWhenMinimumParamsAreValid() {
		String expectedToken = "mockedToken";
		when(jwtTokenUtil.generateAuthenticationToken("cat@meow.com")).thenReturn(expectedToken);

		AuthService.LoggedUser loggedUser = authService.signUp("cat@meow.com", "Meowmeow89", null, List.of());
		User user = loggedUser.getUser();

		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals("cat@meow.com", user.getEmail());
		assertTrue(passwordEncoder.matches("Meowmeow89", user.getPassword()));
		assertNull(user.getName());
		assertTrue(user.getPhones().isEmpty());
		assertNotNull(user.getCreated());
		assertNotNull(user.getLastLogin());
		assertTrue(user.isActive());

		assertEquals(expectedToken, loggedUser.getToken());

		User persistedUser = userRepository.findById(user.getId()).orElseThrow();
		assertEquals(user, persistedUser);
	}

	@Test
	void signUpShouldCreateUserWhenAllParamsAreValid() {
		String expectedToken = "mockedToken";
		when(jwtTokenUtil.generateAuthenticationToken("cat@meow.com")).thenReturn(expectedToken);

		AuthService.LoggedUser loggedUser = authService.signUp(
			"cat@meow.com",
			"Meowmeow89",
			"Snowball",
			List.of(new Phone("12345678", "11", "54"))
		);
		User user = loggedUser.getUser();

		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals("cat@meow.com", user.getEmail());
		assertTrue(passwordEncoder.matches("Meowmeow89", user.getPassword()));
		assertEquals("Snowball", user.getName());
		assertEquals(List.of(new Phone("12345678", "11", "54")), user.getPhones());
		assertNotNull(user.getCreated());
		assertNotNull(user.getLastLogin());
		assertTrue(user.isActive());

		assertEquals(expectedToken, loggedUser.getToken());

		User persistedUser = userRepository.findById(user.getId()).orElseThrow();
		assertEquals(user, persistedUser);
	}

	@Test
	void signUpShouldThrowExceptionWhenEmailAlreadyExists() {
		String expectedToken = "mockedToken";
		when(jwtTokenUtil.generateAuthenticationToken("cat@meow.com")).thenReturn(expectedToken);

		authService.signUp("cat@meow.com", "Meowmeow89", null, List.of());

		EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () ->
			authService.signUp("cat@meow.com", "Meowmeow89", null, List.of())
		);

		assertEquals("User with email cat@meow.com already exists", exception.getMessage());
	}

	@Test
	void loginShouldReturnUserAndTokenWhenCredentialsAreValid() {
		String email = "cat@meow.com";
		String password = "Meowmeow89";
		String encodedPassword = passwordEncoder.encode(password);
		String expectedToken = "mockedToken";

		User user = new User(email, encodedPassword, "Snowball");
		userRepository.saveAndFlush(user);

		when(jwtTokenUtil.generateAuthenticationToken(email)).thenReturn(expectedToken);

		AuthService.LoggedUser loggedUser = authService.login(email, password);

		assertNotNull(loggedUser);
		assertNotNull(loggedUser.getUser());
		assertEquals(email, loggedUser.getUser().getEmail());
		assertEquals(expectedToken, loggedUser.getToken());
	}

	@Test
	void loginShouldThrowExceptionWhenEmailDoesNotExist() {
		InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () ->
			authService.login("nonexistent@meow.com", "Meowmeow89")
		);

		assertEquals("Invalid email or password", exception.getMessage());
	}

	@Test
	void loginShouldThrowExceptionWhenPasswordIsInvalid() {
		String email = "cat@meow.com";
		String password = "Meowmeow89";
		String encodedPassword = passwordEncoder.encode(password);

		User user = new User(email, encodedPassword, "Snowball");
		userRepository.saveAndFlush(user);

		InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () ->
			authService.login(email, "WrongPassword")
		);

		assertEquals("Invalid email or password", exception.getMessage());
	}
}
