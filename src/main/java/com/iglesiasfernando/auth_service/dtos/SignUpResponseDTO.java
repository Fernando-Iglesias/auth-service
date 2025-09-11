package com.iglesiasfernando.auth_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class SignUpResponseDTO {
	private final UUID id;
	private final LocalDateTime created;
	private final LocalDateTime lastLogin;
	private final String token;
	private final boolean isActive;


	public SignUpResponseDTO(UUID id, LocalDateTime created, LocalDateTime lastLogin, String token, boolean isActive) {
		this.id = id;
		this.created = created;
		this.lastLogin = lastLogin;
		this.token = token;
		this.isActive = isActive;
	}

	public UUID getId() {
		return id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public String getToken() {
		return token;
	}

	public boolean isActive() {
		return isActive;
	}
}
