package com.iglesiasfernando.auth_service.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglesiasfernando.auth_service.dtos.LoginRequestDTO;
import com.iglesiasfernando.auth_service.dtos.PhoneDTO;
import com.iglesiasfernando.auth_service.dtos.SignUpRequestDTO;
import com.iglesiasfernando.auth_service.entities.User;
import com.iglesiasfernando.auth_service.repositories.UserRepository;
import com.iglesiasfernando.auth_service.services.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	void signUpShouldReturn201WhenParamsAreValid() throws Exception {
		SignUpRequestDTO request = new SignUpRequestDTO();
		request.setName("Snowball");
		request.setEmail("cat@meow.com");
		request.setPassword("Meowmeow89");
		request.setPhones(Collections.singletonList(new PhoneDTO("12345678", "11", "54")));

		String requestJson = objectMapper.writeValueAsString(request);

		Optional<User> user = userRepository.findByEmail("cat@meow.com");
		assert(user.isEmpty());

		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.created").exists())
			.andExpect(jsonPath("$.lastLogin").exists())
			.andExpect(jsonPath("$.token").exists())
			.andExpect(jsonPath("$.active").value(true));

		user = userRepository.findByEmail("cat@meow.com");

		assert(user.isPresent());
		assert(user.get().getName().equals("Snowball"));
		assert(user.get().isActive());
	}

	@Test
	void signUpShouldReturn409WhenEmailAlreadyExists() throws Exception {
		authService.signUp("cat@meow.com", "Meowmeow89", "Snowball", Collections.emptyList());

		SignUpRequestDTO request = new SignUpRequestDTO();
		request.setName("Snowball");
		request.setEmail("cat@meow.com");
		request.setPassword("Meowmeow89");
		request.setPhones(Collections.singletonList(new PhoneDTO("12345678", "11", "54")));

		String requestJson = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.error", org.hamcrest.Matchers.hasSize(1)))
			.andExpect(jsonPath("$.error[0].timestamp").exists())
			.andExpect(jsonPath("$.error[0].codigo").value(6))
			.andExpect(jsonPath("$.error[0].detail").value("User with email cat@meow.com already exists"));
	}

	@Test
	void signUpShouldReturn400WhenParamsAreInvalid() throws Exception {
		SignUpRequestDTO request = new SignUpRequestDTO();
		request.setName("Snowball");
		request.setEmail("catmeow.com");
		request.setPassword("meowmeow");
		request.setPhones(Collections.singletonList(new PhoneDTO("12345678", "11", "54")));

		String requestJson = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error", org.hamcrest.Matchers.hasSize(2)))
			.andExpect(jsonPath("$.error[0].timestamp").exists())
			.andExpect(jsonPath("$.error[0].codigo").value(2))
			.andExpect(jsonPath("$.error[0].detail").value("Invalid email format"))
			.andExpect(jsonPath("$.error[1].timestamp").exists())
			.andExpect(jsonPath("$.error[1].codigo").value(5))
			.andExpect(jsonPath("$.error[1].detail").value("Password must contain lowercase letters, exactly one uppercase letter and exactly two digits"));
	}

	@Test
	void loginShouldReturn200WhenParamsAreValid() throws Exception {
		authService.signUp("cat@meow.com", "Meowmeow89", "Snowball", Collections.emptyList());

		LoginRequestDTO request = new LoginRequestDTO();
		request.setEmail("cat@meow.com");
		request.setPassword("Meowmeow89");

		String requestJson = objectMapper.writeValueAsString(request);


		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.created").exists())
			.andExpect(jsonPath("$.lastLogin").exists())
			.andExpect(jsonPath("$.token").exists())
			.andExpect(jsonPath("$.active").value(true))
			.andExpect(jsonPath("$.name").value("Snowball"))
			.andExpect(jsonPath("$.email").value("cat@meow.com"))
			.andExpect(jsonPath("$.phones").isArray())
			.andExpect(jsonPath("$.phones").isEmpty());
	}

	@Test
	void loginShouldReturn401WhenCredentialsAreInvalid() throws Exception {
		LoginRequestDTO request = new LoginRequestDTO();
		request.setEmail("cat@meow.com");
		request.setPassword("Meowmeow89");

		String requestJson = objectMapper.writeValueAsString(request);


		mockMvc.perform(post("/api/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isUnauthorized())
		.andExpect(jsonPath("$.error", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(jsonPath("$.error[0].timestamp").exists())
		.andExpect(jsonPath("$.error[0].codigo").value(8))
		.andExpect(jsonPath("$.error[0].detail").value("Invalid email or password"));
	}
}
