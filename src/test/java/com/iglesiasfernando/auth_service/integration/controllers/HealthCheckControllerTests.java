package com.iglesiasfernando.auth_service.integration.controllers;

import com.iglesiasfernando.auth_service.controllers.HealthCheckController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthCheckController.class)
public class HealthCheckControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnHealthCheckUp() throws Exception {
		mockMvc.perform(get("/api/health"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("UP"));
	}
}
