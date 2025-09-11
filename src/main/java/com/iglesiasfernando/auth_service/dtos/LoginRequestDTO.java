package com.iglesiasfernando.auth_service.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LoginRequestDTO {

	@NotBlank(message = "Email is mandatory")
	String email;

	@NotNull(message = "Password is mandatory")
	String password;

	public LoginRequestDTO() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
