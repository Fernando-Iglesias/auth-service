package com.iglesiasfernando.auth_service.dtos;

/**
 * DTO for representing the health check response.
 * Contains the service status.
 */
public class HealthCheckResponseDTO {

	/**
	 * The status of the service (e.g., "UP").
	 */
	private String status;

	public HealthCheckResponseDTO(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
