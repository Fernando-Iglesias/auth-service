package com.iglesiasfernando.auth_service.unit;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.exceptions.EmailAlreadyExistsException;
import com.iglesiasfernando.auth_service.repositories.UserRepository;
import com.iglesiasfernando.auth_service.services.AuthServiceImpl;
import com.iglesiasfernando.auth_service.utils.JwtTokenUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplUnitTests {

	@InjectMocks
	private AuthServiceImpl authService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Test
	void signUpShouldThrowExceptionWhenUkUserEmailViolationOccurs() {
		String email = "cat@meow.com";
		String password = "Meowmeow89";
		String name = "Snowball";
		List<Phone> phones = List.of(new Phone("12345678", "11", "54"));

		when(userRepository.saveAndFlush(any(User.class)))
			.thenThrow(
				new DataIntegrityViolationException(
					"Mocked constraint violation",
					new DataIntegrityViolationException(
						"Mocked constraint violation",
						new ConstraintViolationException(
							"Mocked constraint violation",
							null,
							"UK_USER_EMAIL"
						)
					)
				)
			);

		EmailAlreadyExistsException exception = assertThrows(
			EmailAlreadyExistsException.class,
			() -> authService.signUp(email, password, name, phones)
		);

		assertEquals("User with email " + email + " already exists", exception.getMessage());
	}

	@Test
	void signUpShouldThrowExceptionWhenDataIntegrityViolationOccurs() {
		String email = "cat@meow.com";
		String password = "Meowmeow89";
		String name = "Snowball";
		List<Phone> phones = List.of(new Phone("12345678", "11", "54"));

		when(userRepository.saveAndFlush(any(User.class)))
			.thenThrow(new DataIntegrityViolationException("Mocked constraint violation"));

		DataIntegrityViolationException exception = assertThrows(
			DataIntegrityViolationException.class,
			() -> authService.signUp(email, password, name, phones)
		);

		assertEquals("Mocked constraint violation", exception.getMessage());
	}
}
