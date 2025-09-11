package com.iglesiasfernando.auth_service.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LoginResponseDTO {
	private final UUID id;
	private final LocalDateTime created;
	private final LocalDateTime lastLogin;
	private final String token;
	private final boolean isActive;
	private final String name;
	private final String email;
	private final String password;
	private final List<PhoneDTO> phones;

	public LoginResponseDTO(UUID id, LocalDateTime created, LocalDateTime lastLogin, String token, boolean isActive, String name, String email, String password, List<PhoneDTO> phones) {
		this.id = id;
		this.created = created;
		this.lastLogin = lastLogin;
		this.token = token;
		this.isActive = isActive;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
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

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public List<PhoneDTO> getPhones() {
		return phones;
	}
}
