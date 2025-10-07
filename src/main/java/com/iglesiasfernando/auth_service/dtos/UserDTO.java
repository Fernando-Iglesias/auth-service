package com.iglesiasfernando.auth_service.dtos;

import java.util.List;

public class UserDTO {
	private final String name;
	private final String email;
	private final String password;
	private final List<PhoneDTO> phones;

	public UserDTO(String name, String email, String password, List<PhoneDTO> phones) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
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

