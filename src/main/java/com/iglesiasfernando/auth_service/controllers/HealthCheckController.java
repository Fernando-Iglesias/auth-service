package com.iglesiasfernando.auth_service.controllers;

import com.iglesiasfernando.auth_service.dtos.HealthCheckResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for health check operations.
 * Provides an endpoint to verify the health status of the service.
 */
@RestController
public class HealthCheckController {

	/**
	 * Endpoint to check the health status of the service.
	 *
	 * @return a {@link HealthCheckResponseDTO} containing the health status
	 */
	@GetMapping("/api/health")
	public HealthCheckResponseDTO healthCheck() {
		return new HealthCheckResponseDTO("UP");
	}

}