package com.iglesiasfernando.auth_service.services;

import com.iglesiasfernando.auth_service.entities.Phone;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	public AuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public User signUp(String email, String password, String name, List<Phone> phones) {
		Assert.notNull(email, "Email must not be null");
		Assert.notNull(password, "Password must not be null");
		Assert.notNull(phones, "Phones must not be null");

		User user = new User(email, password, name);
		phones.forEach(phone -> user.addPhone(phone));

		return userRepository.save(user);
	}
}
