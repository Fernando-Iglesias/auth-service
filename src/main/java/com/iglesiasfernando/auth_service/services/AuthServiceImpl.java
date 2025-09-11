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
	private final JwtTokenUtil jwtTokenUtil;
	private static final String EMAIL_UNIQUE_CONSTRAINT = "UK_USER_EMAIL";

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenUtil = jwtTokenUtil;
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

		String token = jwtTokenUtil.generateAuthenticationToken(email);

		return new LoggedUser(persistedUser, token);
	}

	@Override
	@Transactional
	public LoggedUser login(String email, String password) {
		Assert.notNull(email, "Email must not be null");
		Assert.notNull(password, "Password must not be null");

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		user.setLastLogin(LocalDateTime.now());

		String token = jwtTokenUtil.generateAuthenticationToken(email);

		return new LoggedUser(user, token);
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
