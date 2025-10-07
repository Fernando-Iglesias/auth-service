package com.iglesiasfernando.auth_service.services;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.exceptions.EmailAlreadyExistsException;
import com.iglesiasfernando.auth_service.exceptions.InvalidCredentialsException;
import com.iglesiasfernando.auth_service.repositories.UserRepository;
import com.iglesiasfernando.auth_service.utils.JwtTokenUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenUtil jwtUtil;
	private static final String EMAIL_UNIQUE_CONSTRAINT = "UK_USER_EMAIL";

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	@Transactional
	public LoggedUser signUp(String email, String password, String name, List<Phone> phones) {
		Assert.notNull(email, "Email must not be null");
		Assert.notNull(password, "Password must not be null");
		Assert.notNull(phones, "Phones must not be null");

		if (userRepository.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("User with email " + email + " already exists");
		}

		User user = new User(email, passwordEncoder.encode(password), name);
		phones.forEach(phone -> user.addPhone(phone));

		User persistedUser;

		try {
			persistedUser = userRepository.saveAndFlush(user);
		} catch (DataIntegrityViolationException e) {
			// Already checked above, but to be sure in concurrent scenarios
			handleDataIntegrityViolationException(e, email);
			throw e;
		}

		String token = jwtUtil.generateToken(email);

		return new LoggedUser(persistedUser, token);
	}

	@Override
	@Transactional
	public LoggedUser login(String token) {
		String email = jwtUtil.extractUsername(token);
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidCredentialsException("Login failed"));

		if (!user.isActive()) {
			throw new InvalidCredentialsException("Login failed");
		}

		if (!jwtUtil.validateToken(token, email)) {
			throw new InvalidCredentialsException("Login failed");
		}

		String newToken = jwtUtil.generateToken(email);
		user.setLastLogin(LocalDateTime.now());

		return new LoggedUser(user, newToken);
	}


	private void handleDataIntegrityViolationException(DataIntegrityViolationException e, String email) {
		Throwable cause = e.getCause();
		while (cause != null) {
			if (cause instanceof ConstraintViolationException) {
				ConstraintViolationException cve = (ConstraintViolationException) cause;
				String constraintName = cve.getConstraintName();
				if (constraintName != null && constraintName.toUpperCase().contains(EMAIL_UNIQUE_CONSTRAINT)) {
					throw new EmailAlreadyExistsException("User with email " + email + " already exists", e);
				}
			}
			cause = cause.getCause();
		}
	}
}
